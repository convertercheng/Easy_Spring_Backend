package com.qhieco.response.data.api;


import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/15 9:59
 * <p>
 * 类说明：
 *
 */
@Data
public class PayRepData{
    private String orderInfo;
    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageValue;
    private String nonceStr;
    private String timeStamp;
    private String sign;
    private Integer isZero;
    /**
     * mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟。
     */
    private String mwebUrl;

    public PayRepData(){}

    public PayRepData(String orderInfo,String appId,String partnerId,String prepayId,String packageValue,String nonceStr,String timeStamp,String sign){
        this.orderInfo = orderInfo;
        this.appId = appId;
        this.partnerId = partnerId;
        this.prepayId = prepayId;
        this.packageValue = packageValue;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.sign = sign;
    }

}
