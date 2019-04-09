package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ApplyService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.InvoiceMakeRequest;
import com.qhieco.request.api.WithdrawRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.*;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 下午2:51
 * <p>
 *    工单申请Service
 *
 */
@Service
@Slf4j
public class ApplyServiceImpl implements ApplyService{

    @Autowired
    ApplyInvoiceRepository applyInvoiceReository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    OrderParkingRepository orderParkingRepository;

    @Autowired
    BalanceUserRepository balanceUserRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    BankCardRepository bankCardRepository;

    @Autowired
    OrderWithdrawRepository orderWithdrawRepository;

    @Autowired
    OrderTotalRepository orderTotalRepository;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    Task task;

    @Value("${web.upload-path}")
    public String basedir;

    @Value("${INVOICE_URL}")
    private String invoiceUrl;

    @Value("${SERVER_IP_DEV}")
    private String serverIp;

    @Value("${HTTP_PREFIX}")
    private String httpPrefix;

    @Value("${PORT}")
    private String port;

    @Value("${PROJECT_NAME}")
    private String projectName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp makeInvoice(InvoiceMakeRequest invoiceMakeRequest){
        try {
            //验证时间戳是否合法
            if (CommonUtil.isTimeStampInValid(invoiceMakeRequest.getTimestamp())) {
                throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
            }
            Integer mobileUserId = invoiceMakeRequest.getUser_id();
            String phone = invoiceMakeRequest.getPhone();
            Integer type = invoiceMakeRequest.getType();
            String title = invoiceMakeRequest.getTitle();
            String taxpayerId = invoiceMakeRequest.getTaxpayerId();
            String content = invoiceMakeRequest.getContent();
            String email = invoiceMakeRequest.getEmail();
            BigDecimal amount = invoiceMakeRequest.getAmount();
            String orderIds = invoiceMakeRequest.getOrderIds();
            Long now = System.currentTimeMillis();
            if(null == taxpayerId) {
                taxpayerId = "";
            }
            //拆分ids
            String[] idsStr = orderIds.split(Constants.DELIMITER_COMMA);
            List<Integer> ids = new ArrayList<>();
            for(String id : idsStr) {
                ids.add(Integer.valueOf(id));
            }
            //检查是否存在订单
            List<OrderParking> orders = orderParkingRepository.findOrderParkingByIds(ids);
            if(null == orders || orders.size() == 0){
                throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_PARKING);
            }
            //检查订单是否已经开票
            for(OrderParking order : orders){
                if(order.getInvoiceState().intValue() != Status.InvoiceStatus.UNMAKE.getInt()){
                    throw new QhieException(Status.ApiErr.REPEAT_MAKE_INVOICE);
                }
            }
            //先保存到申请表
            ApplyInvoice applyInvoice = new ApplyInvoice(mobileUserId,taxpayerId,orderIds,title,content,amount,type,email,now,Status.Apply.PROCESSING.getInt());
            ApplyInvoice applyInvoiceRes =  applyInvoiceReository.save(applyInvoice);
            if(null == applyInvoiceRes || null == applyInvoiceRes.getId()){
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            Map<String, Object> model = new HashMap<>(16);
            String FPQQLSH  = "qhieco-"+now;
            //发票请求流水号
            model.put("FPQQLSH", FPQQLSH);
            //购方名称
            model.put("GHFMC", title);
            //购货方邮箱
            model.put("GHF_EMAIL", email);
            //购货方识别号
            model.put("GHF_NSRSBH", taxpayerId);
            //购货方手机号
            model.put("GHF_SJ", phone);
            //购货方企业类型
            if(type == 0) {
                //个人
                model.put("GHFQYLX", "03");
            }else {
                //企业
                model.put("GHFQYLX", "01");
            }
            //价税合计金额
            model.put("KPHJJE", amount);

            //合计不含税金额
            String taxRate = configurationFiles.getTaxRate();

            BigDecimal HJBHSJE = amount.divide(Constants.BIGDECIMAL_ONE.add(new BigDecimal(taxRate).divide(Constants.BIGDECIMAL_ONE_HUNDRED)),2,BigDecimal.ROUND_HALF_UP);
            model.put("HJBHSJE", HJBHSJE);
            //税额
            model.put("HJSE", amount.subtract(HJBHSJE));
            //项目单价
            model.put("XMDJ", amount);
            //项目金额
            model.put("XMJE", amount);
            //项目税额
            model.put("SE", amount.subtract(HJBHSJE));
            String contents = TemplateHelper.generate(model, "kpRequestA9.xml");

            // 将content用Aes加密
            String baseContent = AesUtil.encrypt(contents);
            model.put("content", baseContent);
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
            Random random = new Random();
            model.put("dataExchangeId", "P0000001" + sdf.format(new Date()) + random.nextInt(1000000000));
            String xml = TemplateHelper.generate(model, "requestA9.xml");
            Map<String, Object> map = new HashMap<>(16);
            map.put("xml", xml);
            String info = new HttpRequestor().doPost(invoiceUrl, map);
            // 解析返回信息
            XStream xStream = new XStream();
            xStream.alias("interface", KpInterface.class);
            KpInterface kpInterface = XmlHelper1
                    .toBean(info, KpInterface.class);
            String returnCode = kpInterface.getReturnStateInfo()
                    .getReturnCode();
            log.info("returnCode:" + returnCode);
            String returnMessage = com.qhieco.util.Base64.getFromBase64(kpInterface.getReturnStateInfo()
                    .getReturnMessage());
            log.info("returnMessage:"
                    + returnMessage);
            if (Constants.SUCCESS_CODE.equals(returnCode)) {
                log.info("content:"
                        + kpInterface.getData().getContent());
                String resContent = AesUtil.decrypt(kpInterface.getData().getContent());
                log.info("解密后content:"
                        + AesUtil.decrypt(kpInterface.getData().getContent()));
                xStream.alias("RESPONSE_FPKJ", KpInterface.class);
                KpResponse kpResponse =  XmlHelper1.toBean(resContent, KpResponse.class);
                log.info("RETCODE:{}",kpResponse.getRETCODE());
                String pdfUrl = kpResponse.getPDF_URL();
                log.info("pdfUrl:{}",pdfUrl);
                String code = kpResponse.getFP_DM();
                log.info("发票代码:{}",code);
                String number = kpResponse.getFP_HM();
                log.info("发票号码:{}",number);
                //保存发票
                Invoice invoice = new Invoice(code,number,applyInvoice.getId(),Status.Common.VALID.getInt(),now);
                Invoice resInvoice = invoiceRepository.save(invoice);
                if (null == resInvoice || null == resInvoice.getId()){
                    throw new QhieException(Status.ApiErr.INSERT_ERROR);
                }
                //修改订单的发票状态
                orderParkingRepository.updateInvoiceStateById(ids, Status.InvoiceStatus.MAKED_SUCCESS.getInt());
                //修改用户账户中可开票金额
                balanceUserRepository.updateBalanceInvoiceByMobileUserId(amount,mobileUserId,Status.Common.VALID.getInt());
                //另开线程 下载PDF文件和发送邮件
                task.invoiceTask(applyInvoice,pdfUrl);
            }else {
                log.error("开票失败");
                //更新审批结果
                applyInvoiceReository.updateResult(applyInvoiceRes.getId(),null,returnMessage,System.currentTimeMillis(),Status.Apply.PROCESS_FAILED.getInt());
                throw new QhieException(Status.ApiErr.FAIL_MAKE_INVOICE);
            }
        } catch (Exception e) {
            log.error("system error."+e);
            throw new QhieException(Status.ApiErr.UNKNOWN_ERROR);
        }
        return RespUtil.successResp();
    }


    @Override
    public String downLoad(ApplyInvoice applyInvoice,String pdfUrl) {
        log.info("downLoad url:{}",pdfUrl);
        String fileName = applyInvoice.getMobileUserId() + "-"+System.currentTimeMillis()+".pdf";
        String path = basedir.concat("invoice/pdf");
        String filePath = path+"/"+fileName;
        File saveFile = new File(basedir.concat("invoice/pdf"), fileName);
        File parentFile = saveFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //下载pdf到本地
        Integer result = this.saveToFile(pdfUrl, filePath);
        //如果下载成功
        if(result == 1) {
            //pdf转成jpg
            if(this.pfdToJpg( basedir.concat("invoice/pdf/").concat(fileName))){
                Long now = System.currentTimeMillis();
                //保存文件信息到file表
                fileName = fileName.substring(0,fileName.length()-3)+"jpg";
                com.qhieco.commonentity.File file = new com.qhieco.commonentity.File(fileName,"invoice/jpg/"+fileName,Constants.INVOICE_FILE_INTRO,now,Status.Common.VALID.getInt());
                com.qhieco.commonentity.File resFile = fileRepository.save(file);
                if (null == resFile || null == resFile.getId()){
                    throw new QhieException(Status.ApiErr.INSERT_ERROR);
                }
                //构建发票邮件HTML
                String content = this.getInvoiceMailContent(applyInvoice);
                applyInvoiceReository.updateResult(applyInvoice.getId(),resFile.getId(),Constants.SUCCESS_MESSAGE,now,Status.Apply.PROCESS_SUCCESS.getInt());
                this.sendInvoiceEmail(applyInvoice.getEmail(),fileName,content);
            }

        }
        return null;
    }


    public void sendInvoiceEmail(String email,String fileName,String content) {
        log.info("开始调用开票方法email:{},content:{}",email,content);
        String dir = basedir.concat("invoice/jpg");
        File file = new File(dir, fileName);
        MailUtil.getInstance().send(email,Constants.INVOICE_MAIL_TITLE,content,file);
    }


    /**
     * 根据网络地址保存图片
     * @param destUrl 网络地址
     * @param filePath 图片存储路径
     */
    public Integer saveToFile(String destUrl,String filePath) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(destUrl);
            log.info("url.toString():{}",url.toString());
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(filePath);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            return 1;
        } catch (IOException e) {
            log.error("download invoice error."+e);
            return 0;
        } catch (ClassCastException e) {
            log.error("download invoice error."+e);
            return 0;
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (IOException e) {
                log.error("download invoice error."+e);
                return 0;
            } catch (NullPointerException e) {
                log.error("download invoice error."+e);
                return 0;
            }
        }
    }

    /**
     * 构建发票邮件HTML
     * @param applyInvoice
     * @return String
     */
    public String getInvoiceMailContent(ApplyInvoice applyInvoice){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String content = "<body style=\"background-color:#EFEFEF;\">\n" +
                "    <div class=\"mail-content\" style=\"padding:78px 52px 32px;width:1080px;background-color:#fff;margin:0 auto;border-radius:10px;overflow:hidden; position:relative;margin-top:110px;\">\n" +
                "        <div class=\"top-ic\" style=\"position:absolute;width:100%;top:0;left:0;height:26px;background:url('"+ httpPrefix + serverIp + ":"+ port + projectName +"mailPic/topIcon.png') no-repeat center center;background-size:cover;\"></div>\n" +
                "        <div class=\"logo\" style=\"height:70px;background:url('"+httpPrefix + serverIp + ":"+ port + projectName+"mailPic/logo.png') no-repeat left center;\"></div>\n" +
                "        <div class=\"content\" style=\"margin-top:62px;\">\n" +
                "            <div class=\"ct-title\" style=\"font-size:20px;font-weight:bolder;margin-bottom:36px;\">尊敬的客户，您好！</div>\n" +
                "            <div class=\"ct-info\" style=\"padding-bottom:48px;border-bottom:1px solid #CBCBCB;font-size:16px;\">\n" +
                "                您于<span class=\"fue-date\">"+sdf.format(new Date(applyInvoice.getApplyTime())) +"</span>消费并开具了增值税电子发票，请查看附件。\n" +
                "            </div>\n" +
                "            <div class=\"detail\" style=\"margin-top:36px;\">\n" +
                "                <div class=\"dt-info\" style=\"height:34px;line-height:34px;font-size:18px;font-weight:bolder;\">详情</div>\n" +
                "                <div style=\"height:34px;line-height:34px;font-size:18px;\">购方名称：<span class=\"fue-position\">"+ applyInvoice.getTitle() +"</span></div>\n" +
                "                <div style=\"height:34px;line-height:34px;font-size:18px;\">金额合计：<span class=\"amounts\" style=\"color:#F63272;\">￥"+ applyInvoice.getFee() +"</span></div>\n" +
                "                <div style=\"height:34px;line-height:34px;font-size:18px;\">开票日期：<span class=\"fue-date\">"+sdf.format(new Date(applyInvoice.getApplyTime())) +"</span></div>\n" +
                "            </div>\n" +
                "            <div class=\"hot-links\" style=\"margin-top:100px;\">\n" +
                "                <div style=\"height:44px;line-height:44px;font-size:18px;\">客服热线：<a href=\"400-8432-088\" style=\"font-size:18px;color:#0FA5E9;text-decoration:underline;\">400-8432-088</a></div>\n" +
                "                <div style=\"height:44px;line-height:44px;font-size:18px;\">官方网址：<a href=\"www.ahieco.com\" style=\"font-size:18px;color:#0FA5E9;text-decoration:underline;\">www.ahieco.com</a></div>\n" +
                "            </div>\n" +
                "            <div class=\"tips\" style=\"font-size:18px;text-align:center;\">本邮件由系统自动发送，请勿回复</div>\n" +
                "        </div>\n" +
                "        <div class=\"logob\" style=\"position:absolute;bottom:0;right:0;width:240px;height:200px;background:url('"+httpPrefix + serverIp + ":"+ port+ projectName +"mailPic/logob.png') no-repeat center center;background-size:cover;\"></div>\n" +
                "    </div>\n" +
                "    <div class=\"tips\" style=\"font-size:18px;text-align:center;color:#666;margin-top:10px;\">Copyright@2018&nbsp;&nbsp;前海爱翼科技(深圳)有限公司&nbsp;&nbsp;&nbsp;&nbsp;粤ICP17120568</div>\n" +
                "</body>";
        return content;
    }


    /**
     * 提现申请
     * @param withdrawRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp withdraw(WithdrawRequest withdrawRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(withdrawRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer userId = withdrawRequest.getUser_id();
        BalanceUser balanceUser = balanceUserRepository.findByMobileUserIdAndState(userId,Status.Common.VALID.getInt());
        if(null == balanceUser || null == balanceUser.getId()){
            throw new QhieException(Status.ApiErr.NONEXISTENT_BALANCE_USER);
        }

        //查询用户绑定的银行卡
        List<BankCard> bankCards = bankCardRepository.findByMobileUserIdAndState(userId,Status.Common.VALID.getInt());
        if(bankCards.size() == Constants.EMPTY_CAPACITY){
            throw new QhieException(Status.ApiErr.NONEXISTENT_BANK_CARD);
        }
        //生成提现订单号
        String serialNumber = OrderNoGenerator.getOrderNo(Constants.WITHDRAW_ORDER,userId.toString());
        log.info("生成提现订单号："+serialNumber);
        //保存提现订单
        BigDecimal earn = balanceUser.getBalanceEarn();
        Integer bankCardId = bankCards.get(Constants.FIRST_INDEX).getId();
        Long now = System.currentTimeMillis();
        OrderWithdraw orderWithdraw = new OrderWithdraw(serialNumber,userId,earn,now,bankCardId,Status.Withdraw.PROCESSING.getInt());
        OrderWithdraw resOrderWithdraw = orderWithdrawRepository.save(orderWithdraw);
        if(null == resOrderWithdraw || null == resOrderWithdraw.getId()){
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        //改变用户账户数据
        balanceUserRepository.withdraw(earn,userId,Status.Common.VALID.getInt());
        //保存到订单总表
        OrderTotal orderTotal = new OrderTotal(userId,serialNumber,Constants.WITHDRAW_ORDER,now,earn,Status.Withdraw.PROCESSING.getInt());
        OrderTotal resOrderTotal = orderTotalRepository.save(orderTotal);
        if(null == resOrderTotal || null == resOrderTotal.getId()){
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return RespUtil.successResp();
    }


    public boolean pfdToJpg(String path){
        log.info(path);
        File file = new File(path);
        PDDocument doc = new PDDocument();
        try {
            doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                // 方式1,第二个参数是设置缩放比(即像素)
                BufferedImage image = renderer.renderImageWithDPI(i, 296);
                // 方式2,第二个参数是设置缩放比(即像素)
                // BufferedImage image = renderer.renderImage(i, 2.5f);
                path = path.replace("pdf", "jpg");
                File fileNew = new File(path);
                File parentFile = fileNew.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                ImageIO.write(image, "jpg", new File(path));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally
        {
            if(doc!= null )
            {
                try {
                    doc.close();
                }catch (IOException e){
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }


    @Component
    class Task {

        @Autowired
        private ApplyService applyService;

        @Async
        public void invoiceTask(ApplyInvoice applyInvoice, String pdfUrl)  {
            applyService.downLoad(applyInvoice,pdfUrl);
        }

    }



}
