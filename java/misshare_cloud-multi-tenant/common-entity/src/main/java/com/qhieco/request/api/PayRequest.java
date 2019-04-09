package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 支付请求参数
 */
@Data
public class PayRequest extends AbstractRequest{
    private Integer order_id;
    /**
     * 1 支付宝    2 微信
     */
    private Integer channel;
    private Integer coupon_id;
    /**
     * 5为html5   安卓苹果1
     */
    private Integer type;
    private String wapUrl;
    private String spbillCreateIp;
    private String openId;


}
