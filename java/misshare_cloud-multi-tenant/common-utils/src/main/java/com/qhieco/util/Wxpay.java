package com.qhieco.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * Created by xujiayu on 17/9/17.
 */
@XStreamAlias(value = "xml")
@Data
public class Wxpay {
    private String appid;
    private String body;
    @XStreamAlias(value = "mch_id")
    private String mchId;
    @XStreamAlias(value = "nonce_str")
    private String nonceStr;
    @XStreamAlias(value = "notify_url")
    private String notifyUrl;
    @XStreamAlias(value = "out_trade_no")
    private String outTradeNo;
    @XStreamAlias(value = "spbill_create_ip")
    private String spbillCreateIp;
    @XStreamAlias(value = "total_fee")
    private Integer totalFee;
    @XStreamAlias(value = "trade_type")
    private String tradeType;
    private String sign;
	@XStreamAlias(value = "scene_info")
	private String sceneInfo;
    
    @XStreamAlias(value = "return_code")
    private String returnCode;
    @XStreamAlias(value = "result_code")
    private String resultCode;
    private String openid;
    @XStreamAlias(value = "bank_type")
    private String bankType;
    @XStreamAlias(value = "cash_fee")
    private Integer cashFee;
    @XStreamAlias(value = "transaction_id")
    private String transactionId;
    @XStreamAlias(value = "time_end")
    private String timeEnd;
    @XStreamAlias(value = "out_refund_no")
    private String outRefundNo;
    @XStreamAlias(value = "refund_fee")
    private Integer refundFee;
    @XStreamAlias(value = "refund_account")
    private String refundAccount;


}
