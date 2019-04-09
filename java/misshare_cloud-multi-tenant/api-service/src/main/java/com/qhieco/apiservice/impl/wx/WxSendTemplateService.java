package com.qhieco.apiservice.impl.wx;

import com.qhieco.apiservice.impl.redis.RedisService;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.commonrepo.OrderParkingRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.util.DateUtils;
import com.qhieco.util.JsonUtils;
import com.qhieco.util.TemplateMsgResult;
import com.qhieco.util.WechatTemplateMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TreeMap;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/17 17:11
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class WxSendTemplateService {

    @Autowired
    private WxService wxService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    /**
     * 支付预约或停车费成功微信模板推送
     * @param title
     * @param payTime
     * @param total
     * @param openId
     * @param orderId
     * @return
     */
    public TemplateMsgResult sendPayTemplate(String title, String payTime, BigDecimal total, String openId, Integer orderId){
        log.info("调用支付成功通知模板信息：title：="+title+"payTime:="+payTime+"金额:"+total+"通知用户："+openId+"订单id:"+orderId);
        TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
        //根据具体模板参数组装
        params.put("first",WechatTemplateMsg.item(title, "#000000"));
        params.put("keyword1",WechatTemplateMsg.item(payTime, "#000000"));
        params.put("keyword2", WechatTemplateMsg.item(total+"元", "#000000"));
        params.put("remark",WechatTemplateMsg.item("查看订单详情", "#000000"));
        WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
        wechatTemplateMsg.setTemplate_id("o5DCysY5Q87mCxOjq7oRYSmGnVak2gxsezE7yu0mQWc");
        wechatTemplateMsg.setTouser(openId);
        wechatTemplateMsg.setUrl(getUrl(orderId));
        wechatTemplateMsg.setData(params);
        String data = JsonUtils.toJsonString(wechatTemplateMsg);
        String accessToken=wxService.replaceAccessToken();
        log.info("accessToken{}", accessToken);
        log.info("wechatTemplateMsg{}",data);
        TemplateMsgResult templateMsgResult =  wxService.sendTemplate(accessToken,data);
        return templateMsgResult;
    }

    /**
     * 退款通知
     * @param title
     * @param description
     * @param refundTime
     * @param total
     * @param openId
     * @param orderId
     * @return
     */
    public TemplateMsgResult sendRefundTemplate(String title, String description, String refundTime, BigDecimal total, String openId, Integer orderId){
        log.info("调用支付成功通知模板信息：title：="+title+"refundTime:="+refundTime+"金额:"+total+"通知用户："+openId+"订单id:"+orderId);
        TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
        //根据具体模板参数组装
        params.put("first",WechatTemplateMsg.item(title, "#000000"));
        params.put("keyword1",WechatTemplateMsg.item(description, "#000000"));
        params.put("keyword2",WechatTemplateMsg.item(total+"元", "#000000"));
        params.put("keyword3",WechatTemplateMsg.item(refundTime, "#000000"));
        params.put("remark",WechatTemplateMsg.item("感谢您的支持，祝您生活愉快！", "#000000"));
        WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
        wechatTemplateMsg.setTemplate_id("BG6l3mKZU2QXPwGj2SrDdhJD5lrJsHG52ynWUO8Doyw");
        wechatTemplateMsg.setTouser(openId);
        wechatTemplateMsg.setData(params);
        String data = JsonUtils.toJsonString(wechatTemplateMsg);
        String accessToken=wxService.replaceAccessToken();
        log.info("accessToken{}", accessToken);
        log.info("wechatTemplateMsg{}",data);
        TemplateMsgResult templateMsgResult =  wxService.sendTemplate(accessToken,data);
        return templateMsgResult;
    }


    /**
     * 入场通知
     * @param plateNumber
     * @param parklotName
     * @param startTime
     * @param openId
     * @param orderId
     * @return
     */
    public TemplateMsgResult sendEnterTemplate(String plateNumber,String parklotName,String startTime,String openId,Integer orderId){
        log.info("调用入场提醒通知模板信息：title：=尊敬的车主，您的预约订单将到入场时间，请您及时入场。"+"plateNumber:="+plateNumber+"parklotName:"+parklotName+"startTime："+startTime);
        TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
        //根据具体模板参数组装
        params.put("first",WechatTemplateMsg.item("尊敬的车主，您的预约订单将到入场时间，请您及时入场。", "#000000"));
        params.put("keyword1",WechatTemplateMsg.item(plateNumber, "#000000"));
        params.put("keyword2",WechatTemplateMsg.item(parklotName, "#000000"));
        params.put("keyword3",WechatTemplateMsg.item(startTime, "#000000"));
        params.put("remark",WechatTemplateMsg.item("欢迎光临！祝您生活愉快！", "#000000"));
        WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
        wechatTemplateMsg.setTemplate_id("zpl6uGW9GKYv379EUR32IP0kkVw64Gl_a6NEQcUdoJs");
        wechatTemplateMsg.setTouser(openId);
        wechatTemplateMsg.setUrl(getUrl(orderId));
        wechatTemplateMsg.setData(params);
        String data = JsonUtils.toJsonString(wechatTemplateMsg);
        String accessToken=wxService.replaceAccessToken();
        TemplateMsgResult templateMsgResult =  wxService.sendTemplate(accessToken,data);
        return templateMsgResult;
    }


    /**
     * 离场通知
     * @param plateNumber
     * @param parklotName
     * @param endTime
     * @param openId
     * @param orderId
     * @return
     */
    public TemplateMsgResult sendLeaveTemplate(String plateNumber,String parklotName,String endTime,String openId,Integer orderId){
        log.info("调用入场提醒通知模板信息：title：=尊敬的车主，您的预约订单将到出场时间，请您及时出场。"+"plateNumber:="+plateNumber+"parklotName:"+parklotName+"endTime："+endTime);
        TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
        //根据具体模板参数组装
        params.put("first",WechatTemplateMsg.item("尊敬的车主，您的预约订单将到出场时间，请您及时出场。", "#000000"));
        params.put("keyword1",WechatTemplateMsg.item(plateNumber, "#000000"));
        params.put("keyword2",WechatTemplateMsg.item(parklotName, "#000000"));
        params.put("keyword3",WechatTemplateMsg.item(endTime, "#000000"));
        params.put("remark",WechatTemplateMsg.item("祝您出行顺利，一路平安！", "#000000"));
        WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
        wechatTemplateMsg.setTemplate_id("JdAbCwuH83zmgPQkE74zyiSsdPDINuz1nllqljnDDEs");
        wechatTemplateMsg.setTouser(openId);
        wechatTemplateMsg.setUrl(getUrl(orderId));
        wechatTemplateMsg.setData(params);
        String data = JsonUtils.toJsonString(wechatTemplateMsg);
        String accessToken=wxService.replaceAccessToken();
        TemplateMsgResult templateMsgResult =  wxService.sendTemplate(accessToken,data);
        return templateMsgResult;
    }

    /**
     * 模板跳转的订单地址
     * @param orderId
     * @return
     */
    private String getUrl(Integer orderId){
        OrderParking orderParking=orderParkingRepository.findOne(orderId);
        return configurationFiles.getHttpPrefix()+configurationFiles.getServerIpDev()+"/apiwrite/wechat/author?type=kh5&parklotId="+orderParking.getParklotId();
    }
}
