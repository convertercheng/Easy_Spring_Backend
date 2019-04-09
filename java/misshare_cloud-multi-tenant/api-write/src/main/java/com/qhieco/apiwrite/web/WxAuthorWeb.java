package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.UserService;
import com.qhieco.apiservice.WxAuthorService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.WxBindRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.WxSmallRoutineData;
import com.qhieco.util.CusAccessObjectUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.util.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.bean.xmlmessage.XMLNewsMessage;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;
import weixin.popular.util.SignatureUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/25 17:27
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
public class WxAuthorWeb {

    private static ExpireKey expireKey = new DefaultExpireKey();

    @Autowired
    WxAuthorService wxAuthorService;
    @Autowired
    ConfigurationFiles configurationFiles;

    @Autowired
    UserService userService;

    /**
     * 微信授权统一路口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/author")
    public ModelAndView author(HttpServletRequest request) throws Exception {
        String type = request.getParameter("type");
        String parklotId = request.getParameter("parklotId");
        String appId = configurationFiles.getWxMpAppId();
        String http = configurationFiles.getHttpPrefix() + configurationFiles.getServerIpDev() + "/apiwrite";
        if ("h5".equals(type)) {
            String url = http + "/wechat/h5/author";
            url = Constants.WX_AUTHOR_URL.replace("APPID", appId).replace("REDIRECT_URI", url).replace("STATE", parklotId);
            return new ModelAndView("redirect:" + url);
        }
        if ("kh5".equals(type)) {
            String url = http + "/wechat/kh5/author";
            url = Constants.WX_AUTHOR_URL.replace("APPID", appId).replace("REDIRECT_URI", url).replace("STATE", parklotId);
            return new ModelAndView("redirect:" + url);
        }
        if ("scanpay".equals(type)) {
            String url = http + "/wechat/scanpay/author";
            url = Constants.WX_AUTHOR_URL.replace("APPID", appId).replace("REDIRECT_URI", url).replace("STATE", parklotId);
            return new ModelAndView("redirect:" + url);
        }
        if ("orderAuthor".equals(type)) {
            String url = http + "/wechat/orderAuthor";
            url = Constants.WX_AUTHOR_URL.replace("APPID", appId).replace("REDIRECT_URI", url).replace("STATE", parklotId);
            return new ModelAndView("redirect:" + url);
        }
        if ("userAuthor".equals(type)) {
            String url = http + "/wechat/userAuthor";
            url = Constants.WX_AUTHOR_URL.replace("APPID", appId).replace("REDIRECT_URI", url).replace("STATE", parklotId);
            return new ModelAndView("redirect:" + url);
        }
        return null;

    }

    /**
     * H5预约入口
     *
     * @param request 请求参数
     * @return ModelAndView 视图
     * @throws Exception
     */
    @GetMapping("/h5/author")
    public ModelAndView hFivePlusauthor(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("code=" + code);
        Map<String, String> wxMap = null;
        log.info("H5预约入口，授权回调接口， code = " + code + ", state = " + state);
        try {
            if (StringUtils.isNotEmpty(code)) {
                wxMap = WxUtil.getOAuthOpenId(code, configurationFiles.getWxMpAppId(), configurationFiles.getWxMpSecret());
                log.info("wxMap=" + wxMap);
            }
            return new ModelAndView("redirect:" + userService.findByUserExtraInfo(wxMap, state) + "&code=1000");
        } catch (Exception e) {
            log.error("未获取到code或state....");
        }
        return null;
    }

    /**
     * 快速预约H5入口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/kh5/author")
    public ModelAndView khFivePlusauthor(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String url = "";
        log.info("快速预约H5入口，授权回调接口， code = " + code + ", state = " + state);
        try {
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(state)) {
                url = wxAuthorService.wxAuthorLocation(code, state, CusAccessObjectUtil.getIpAddress(request));
                log.info(" 微信扫描跳转地址为 ：" + url);
                return new ModelAndView("redirect:" + url);
            }
        } catch (Exception e) {
            log.error("未获取到code或state....");
        }
        return null;
    }

    /**
     * 小程序授权获取openID
     *
     * @param wxBindRequest 请求参数
     * @throws Exception
     */
    @PostMapping("/xcx/getOpenId")
    public Resp getOpenId(@RequestBody WxBindRequest wxBindRequest) throws Exception {
        String code = wxBindRequest.getCode();
        String appId = wxBindRequest.getAppId();
        log.info("code=" + code);
        String openId = null;
        log.info("小程序入口，授权回调接口， code = " + code + ", appId = " + appId);
        try {
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(appId)) {
                openId = WxUtil.getOAuthXcxOpenId(code, appId, configurationFiles.getWxXcxSecret());
                log.info("openId=" + openId);
            }
        } catch (Exception e) {
            log.error("未获取到code或appId....");
        }
        return RespUtil.successResp(openId);
    }

    /**
     * 扫码支付快速入口
     *
     * @return
     */
    @GetMapping(value = "scanpay/author")
    public ModelAndView scanpayAuthor(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("扫码支付停车费，扫码授权回调接口， code = " + code + ", state = " + state);
        try {
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(state)) {
                String url = wxAuthorService.scanpayAuthorRedirectUrl(code, state);
                return new ModelAndView("redirect:" + url);
            }
        } catch (Exception e) {
            log.error("扫码支付停车费，扫码授权回调接口 异常 ：" + e);
        }
        return null;
    }

    /**
     * 新用户绑定
     *
     * @param wxBindRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/saveUserBind")
    public Resp saveUserBind(@RequestBody WxBindRequest wxBindRequest) throws Exception {
        Resp checkResp = ParamCheck.check(wxBindRequest, "plateNumber", "phone", "timestamp", "startTime",
                "endTime", "totalFee", "parklotId", "shareStartTime", "shareEndTime");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return wxAuthorService.saveUserBind(wxBindRequest);
    }

    /**
     * 授权获取微信openId跳转页面
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/order")
    public ModelAndView order(HttpServletRequest request) throws Exception {
        String openId = request.getParameter("openId");
        String url = "";
        if (StringUtils.isNotEmpty(openId)) {
            url = wxAuthorService.wxAuthorOrderLocation(openId);
            log.info(" 微信扫描跳转地址为 ：" + url);
            if (StringUtils.isEmpty(url)) {
                url = configurationFiles.getMpAuthUrl();
            }
            return new ModelAndView("redirect:" + url);
        }
        return null;
    }

    /**
     * 授权获取微信openId跳转订单页面
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/orderAuthor")
    public ModelAndView orderAuthor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String url = "";
        if (StringUtils.isNotEmpty(code)) {
            Map<String, String> map = WxUtil.getOAuthOpenId(code, configurationFiles.getWxMpAppId(), configurationFiles.getWxMpSecret());
            url = wxAuthorService.wxAuthorOrderLocation(map.get("openId"));
            if (StringUtils.isEmpty(url)) {
                url = configurationFiles.getMpUrlPrefix();
            }
            log.info(" 微信扫描跳转地址为 ：" + url);
            return new ModelAndView("redirect:" + url);
        }
        log.error("未获取到code....");
        return null;
    }

    /**
     * 授权获取微信openId跳转订单页面
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/hnAuthor")
    public ModelAndView hnAuthor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = configurationFiles.getMpAuthUrl();
        log.info(" 微信扫描跳转地址为 ：" + url);
        return new ModelAndView("redirect:" + url);
    }


    /**
     * 授权获取微信openId跳转页面
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/userOrder")
    public ModelAndView userOrder(HttpServletRequest request) throws Exception {
        String orderId = request.getParameter("orderId");
        String url = "";
        if (StringUtils.isNotEmpty(orderId)) {
            url = wxAuthorService.wxUserOrderLocation(orderId);
            log.info(" 微信扫描跳转地址为 ：" + url);
            if (StringUtils.isEmpty(url)) {
                url = configurationFiles.getMpAuthUrl();
            }
            return new ModelAndView("redirect:" + url);
        }
        return null;
    }

    /**
     * 授权获取微信openId跳转用户信息页面
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/userAuthor")
    public ModelAndView userAuthor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String url = "";
        if (StringUtils.isNotEmpty(code)) {
            url = wxAuthorService.wxAuthorLocation(code);
            log.info(" 微信扫描跳转地址为 ：" + url);
            return new ModelAndView("redirect:" + url + "&code=1001");
        }
        log.error("未获取到code....");
        return null;
    }

    /**
     * 获取微信sessionkey
     *
     * @param wxBindRequest
     * @return
     */
    @PostMapping("/getSeesionKey")
    public Resp getSeesionKey(@RequestBody WxBindRequest wxBindRequest) {
        String code = wxBindRequest.getCode();
        String appId = wxBindRequest.getAppId();
        String secret = wxBindRequest.getSecret();
        try {
            WxSmallRoutineData result = WxUtil.getSmallRoutineAuthor(code, appId, secret);
            Map<String, String> sessionMap = new HashMap<>();
            sessionMap.put("session_key", result.getSession_key() + result.getOpenid());
            return RespUtil.successResp(sessionMap);
        } catch (Exception e) {
            log.error("获取SeesionKey错误", e);
        }
        return null;
    }

    /**
     * 小程序解密获取数据
     *
     * @param wxBindRequest 请求参数
     * @throws Exception
     */
    @PostMapping("/small/routine/author")
    public Resp author(@RequestBody WxBindRequest wxBindRequest){
        Resp checkResp = ParamCheck.check(wxBindRequest, "encryptedData", "iv", "sessionKey");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return wxAuthorService.getSmallRoutineAuthorInfo(wxBindRequest);
    }

    @PostMapping("/yzToken")
    public void yzToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        //首次请求申请验证,返回echostr
        if (echostr != null) {
            outputStreamWrite(outputStream, echostr);
            return;
        }

        //验证请求签名
        if (!signature.equals(SignatureUtil.generateEventMessageSignature(configurationFiles.getWxMpSecret(), timestamp, nonce))) {
            System.out.println("The request signature is invalid");
            return;
        }

        boolean isreturn = false;
        log.info("1.收到微信服务器消息");
        Map<String, String> wxdata = parseXml(request);
        if (wxdata.get("MsgType") != null) {
            if ("event".equals(wxdata.get("MsgType"))) {
                log.info("2.1解析消息内容为：事件推送");
                if ("subscribe".equals(wxdata.get("Event"))) {
                    log.info("2.2用户第一次关注 返回true哦");
                    isreturn = true;
                }
            }
        }

        if (isreturn) {
            //转换XML
            String key = wxdata.get("FromUserName") + "__"
                    + wxdata.get("ToUserName") + "__"
                    + wxdata.get("MsgId") + "__"
                    + wxdata.get("CreateTime");
            log.info("3.0 进入回复 转换对象：" + key);

            if (expireKey.exists(key)) {
                //重复通知不作处理
                log.info("3.1  重复通知了");
                return;
            } else {
                log.info("3.1  第一次通知");
                expireKey.add(key);
            }

            log.info("3.2  回复你好");
            //创建回复
            XMLNewsMessage.Article article = new XMLNewsMessage.Article();
            article.setUrl(configurationFiles.getHttpPrefix() + configurationFiles.getServerIpDev() + "/apiwrite/wechat/order?openId=" + wxdata.get("FromUserName"));
            article.setDescription("如果您已有预约车位，点击这里查看订单详情");
            article.setPicurl(configurationFiles.getPicPath() + "wx/active.jpg");
            article.setTitle("查看订单详情");
            List<XMLNewsMessage.Article> articles = new ArrayList<>();
            articles.add(article);
            XMLMessage xmlTextMessage = new XMLNewsMessage(wxdata.get("FromUserName"), wxdata.get("ToUserName"), articles);
            xmlTextMessage.outputStreamWrite(outputStream);
            return;
        }
        log.info("3.2  回复空");
        outputStreamWrite(outputStream, "");

    }

    /**
     * 数据流输出
     *
     * @param outputStream
     * @param text
     * @return
     */
    private boolean outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * dom4j 解析 xml 转换为 map
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * 回复微信服务器"文本消息"
     *
     * @param response
     * @param returnvaleue
     */
    public void output(HttpServletResponse response, String returnvaleue) {
        try {
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
            log.info("****************return valeue***************=" + returnvaleue);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
