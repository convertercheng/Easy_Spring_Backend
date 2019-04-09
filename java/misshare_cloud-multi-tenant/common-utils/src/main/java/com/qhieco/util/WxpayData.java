package com.qhieco.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;


@XStreamAlias(value = "xml")
@Data
public class WxpayData {
    @XStreamAlias(value = "return_code")
    private String returnCode;
    @XStreamAlias(value = "return_msg")
    private String returnMsg;
    private String appid;
    @XStreamAlias(value = "mch_id")
    private String mchId;
    @XStreamAlias(value = "nonce_str")
    private String nonceStr;
    private String sign;
    @XStreamAlias(value = "result_code")
    private String resultCode;
    @XStreamAlias(value = "prepay_id")
    private String prepayId;
    @XStreamAlias(value = "trade_type")
    private String tradeType;
    @XStreamAlias(value = "bank_type")
    private String bankType;
    @XStreamAlias(value = "cash_fee")
    private Integer cashFee;
    @XStreamAlias(value = "fee_type")
    private String feeType;
    @XStreamAlias(value = "is_subscribe")
    private String isSubscribe;
    private String openid;
    @XStreamAlias(value = "out_trade_no")
    private String outTradeNo;
    @XStreamAlias(value = "time_end")
    private String timeEnd;
    @XStreamAlias(value = "total_fee")
    private Integer totalFee;
    @XStreamAlias(value = "transaction_id")
    private String transactionId;
    @XStreamAlias(value = "refund_fee")
    private Integer refundFee;
    @XStreamAlias(value = "out_refund_no")
    private String outRefundNo;
    @XStreamAlias(value = "refund_id")
    private String refundId;
    @XStreamAlias(value = "refund_channel")
    private String refundChannel;
    @XStreamAlias(value = "coupon_refund_fee")
    private Integer couponRefundFee;
    @XStreamAlias(value = "coupon_refund_count")
    private Integer couponRefundCount;
    @XStreamAlias(value = "cash_refund_fee")
    private Integer cashRefundFee;
	@XStreamAlias(value = "mweb_url")
	private String mwebUrl;
    @XStreamAlias(value = "coupon_fee")
    private Integer couponFee;
    @XStreamAlias(value = "coupon_count")
    private Integer couponCount;
    @XStreamAlias(value = "coupon_id_0")
    private String couponId0;
    @XStreamAlias(value = "coupon_fee_0")
    private Integer couponFee0;





	@Override
	public String toString() {
		return "WxpayData [returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", appid=" + appid + ", mchId="
				+ mchId + ", nonceStr=" + nonceStr + ", sign=" + sign + ", resultCode=" + resultCode + ", prepayId="
				+ prepayId + ", tradeType=" + tradeType + ", bankType=" + bankType + ", cashFee=" + cashFee
				+ ", feeType=" + feeType + ", isSubscribe=" + isSubscribe + ", openid=" + openid + ", outTradeNo="
				+ outTradeNo + ", timeEnd=" + timeEnd + ", totalFee=" + totalFee + ", transactionId=" + transactionId
				+ ", refundFee=" + refundFee + ", outRefundNo=" + outRefundNo + ", refundId=" + refundId
				+ ", refundChannel=" + refundChannel + ", couponRefundFee=" + couponRefundFee + ", couponRefundCount="
				+ couponRefundCount + ", cashRefundFee=" + cashRefundFee + ", mwebUrl=" + mwebUrl + ", couponFee=" + couponFee +
                ", couponCount=" + couponCount +", couponId0=" + couponId0 +", couponFee0=" + couponFee0 +"]";
	}
}
