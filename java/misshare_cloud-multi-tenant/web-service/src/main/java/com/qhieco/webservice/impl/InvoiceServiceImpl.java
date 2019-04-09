package com.qhieco.webservice.impl;

import com.qhieco.commonentity.ApplyInvoice;
import com.qhieco.commonentity.Invoice;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonrepo.ApplyInvoiceRepository;
import com.qhieco.commonrepo.InvoiceRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.RedInvoiceRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.*;
import com.qhieco.webservice.InvoiceService;
import com.qhieco.webservice.exception.QhieWebException;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:51
 * <p>
 * 类说明：
 *     发票逻辑层
 */
@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    ApplyInvoiceRepository applyInvoiceRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Value("${INVOICE_URL}")
    private String invoiceUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp redInvoice(RedInvoiceRequest redInvoiceRequest){
        Integer applyId = redInvoiceRequest.getApplyId();
        String redReason = redInvoiceRequest.getRedReason();
        String pdfUrl = "";
        log.info("applyId:{},redReason:{}",applyId,redReason);
        Invoice invoice = invoiceRepository.findByApplyId(applyId);
        ApplyInvoice applyInvoice = applyInvoiceRepository.findOne(applyId);
        if(null == invoice || null == applyInvoice){
            throw new QhieWebException(Status.WebErr.ILLEGAL_ALL);
        }
        if(applyInvoice.getState().equals(Status.Apply.PROCESS_RED.getInt())){
            throw new QhieWebException(Status.WebErr.RED_INVOICE_DUPLICATE);
        }
        String code = invoice.getCode();
        String number = invoice.getNumber();
        try {
            Long now = System.currentTimeMillis();
            Map<String, Object> model = new HashMap<String, Object>();
            String FPQQLSH  = "qhieco-"+now;
            //发票请求流水号
            model.put("FPQQLSH", FPQQLSH);
            log.info("流水号:" + FPQQLSH);
            //购方名称
            model.put("GHFMC", applyInvoice.getTitle());
            String taxpayerId = "";
            if(applyInvoice.getTaxpayerId() != null) {
                taxpayerId = applyInvoice.getTaxpayerId();
            }
            //购货方邮箱
            model.put("GHF_EMAIL", applyInvoice.getEmail());
            //购货方识别号
            model.put("GHF_NSRSBH", taxpayerId);
            //购货方手机号
            UserMobile userMobile = userMobileRepository.findOne(applyInvoice.getMobileUserId());
            model.put("GHF_SJ", userMobile.getPhone());
            //购货方企业类型
            if(applyInvoice.getType().equals(0)) {
                model.put("GHFQYLX", "03");
            }else {
                model.put("GHFQYLX", "01");
            }
            //价税合计金额
            model.put("KPHJJE", applyInvoice.getFee().multiply(new BigDecimal(-1)));
            log.info("价税合计金额:" + applyInvoice.getFee().multiply(new BigDecimal(-1)));
            //合计不含税金额
            String taxRate = configurationFiles.getTaxRate();
            BigDecimal HJBHSJE = applyInvoice.getFee().divide(Constants.BIGDECIMAL_ONE.add(new BigDecimal(taxRate).divide(Constants.BIGDECIMAL_ONE_HUNDRED)),2,BigDecimal.ROUND_HALF_UP);
            model.put("HJBHSJE", HJBHSJE.multiply(new BigDecimal(-1)));
            log.info("合计不含税金额:" + HJBHSJE.multiply(new BigDecimal(-1)));
            //税额
            model.put("HJSE", applyInvoice.getFee().subtract(HJBHSJE).multiply(new BigDecimal(-1)));
            log.info("税额:{}",applyInvoice.getFee().subtract(HJBHSJE).multiply(new BigDecimal(-1)));
            //原发票代码
            model.put("YFP_DM", code);
            log.info("原发票代码:{}",code);
            //原发票号码
            model.put("YFP_HM", number);
            log.info("原发票号码:{}",number);
            //冲红原因
            model.put("CHYY", redReason);
            log.info("冲红原因:{}",redReason);
            String remark = "对应正数发票代码:"+code+" 号码:"+number;
            //备注
            model.put("BZ", remark);
            log.info("备注:{}",remark);
            //项目单价
            model.put("XMDJ", applyInvoice.getFee().multiply(new BigDecimal(-1)));
            log.info("项目单价:{}",applyInvoice.getFee().multiply(new BigDecimal(-1)));
            //项目金额
            model.put("XMJE", applyInvoice.getFee().multiply(new BigDecimal(-1)));
            log.info("项目单价:{}",applyInvoice.getFee().multiply(new BigDecimal(-1)));
            //项目税额
            model.put("SE",applyInvoice.getFee().subtract(HJBHSJE).multiply(new BigDecimal(-1)));
            String contents = TemplateHelper.generate(model, "kpRequestA9.xml");

            // 将content用Aes加密
            String baseContent = AesUtil.encrypt(contents);
            model.put("content", baseContent);
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
            Random random = new Random();
            model.put("dataExchangeId", "P0000001" + sdf.format(new Date()) + random.nextInt(1000000000));
            String xml = TemplateHelper.generate(model, "requestA9.xml");
            Map<String, Object> map = new HashMap<String, Object>();
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
            log.info("returnMessage:"
                    + Base64.getFromBase64(kpInterface.getReturnStateInfo()
                    .getReturnMessage()));
            if (returnCode.equals("0000")) {
                log.info("content:"
                        + kpInterface.getData().getContent());
                String resContent = AesUtil.decrypt(kpInterface.getData().getContent());
                log.info("解密后content:"
                        + AesUtil.decrypt(kpInterface.getData().getContent()));
                xStream.alias("RESPONSE_FPKJ", KpInterface.class);
                KpResponse kpResponse =  XmlHelper1.toBean(resContent, KpResponse.class);
                log.info("RETCODE:{}",kpResponse.getRETCODE());
                pdfUrl = kpResponse.getPDF_URL();
                log.info("pdfUrl:{}",pdfUrl);
                applyInvoiceRepository.updateRed(applyId,Status.Apply.PROCESS_RED.getInt());
            }else {
                log.error("开票失败");
                throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
            }
        } catch (Exception e) {
            log.error("system error."+e);
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
        return RespUtil.successResp(pdfUrl);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp left(){
        String cnt = "";
        try {
            Map<String, Object> model = new HashMap<>(32);
            String content = TemplateHelper.generate(model, "kccxRequest.xml");
            // 将content用Aes加密
            String baseContent = AesUtil.encrypt(content);

            model.put("content", baseContent);
            // 生成完整请求信息
            // 开票接口时request中需要将request.xml中interfaceCode修改为DFXJ1009,下载时为：ECXML.FPXZ.CX.E_INV
            // ;普通开票为DFXJ1007
            String xml = TemplateHelper.generate(model, "requestkc.xml");
            Map<String, Object> map = new HashMap<>(16);
            map.put("xml", xml);
            // 请求地址
            String url=invoiceUrl;
            String info = new HttpRequestor().doPost(url, map);
            log.info(info);
            // 解析返回信息
            XStream xStream = new XStream();
            xStream.alias("interface", KpInterface.class);
            KpInterface kpInterface = XmlHelper1
                    .toBean(info, KpInterface.class);
            String returnCode = kpInterface.getReturnStateInfo()
                    .getReturnCode();
            log.info("returnCode:" + returnCode);
            log.info("returnMessage:"
                    + Base64.getFromBase64(kpInterface.getReturnStateInfo()
                    .getReturnMessage()));
            if (returnCode.equals("0000")) {
                log.info("content:"+ kpInterface.getData().getContent());
                String resContent = AesUtil.decrypt(kpInterface.getData().getContent());
                log.info("解密后content:" + resContent);
                KcResponse kcResponse =  XmlHelper1.toBean(resContent, KcResponse.class);
                log.info("RETCODE:{}",kcResponse.getRETURNCODE());
                cnt = kcResponse.getCNT();
                log.info("cnt:{}",cnt);
            }
        } catch (Exception e) {
            log.error("SYSTEM ERROR."+e);
            e.printStackTrace();
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
        return RespUtil.successResp(cnt);
    }

}
