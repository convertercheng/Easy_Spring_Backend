package com.qhieco.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qhieco.constant.Constants;
import org.springframework.util.StringUtils;

/**
 * Created by xujiayu on 17/9/17.
 */
public class MapUtil {
    public static String join(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        int size = keyList.size();
        for (int i = 0; i < size; i++) {
            String key = keyList.get(i);
            String value = map.get(key);
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            if (i == size - 1) {
                stringBuilder.append(key).append("=").append(value);
            }
            else {
                stringBuilder.append(key).append("=").append(value).append("&");
            }
        }
        return stringBuilder.toString();
    }
    
    /**
     * map装换成xml
     * @param map 参数map
     * @return String
     */
	public static String mapToXML(Map<String, String> map) {
		StringBuilder builder = new StringBuilder("<xml>");
		for (String key : map.keySet()) {
			if (null != map.get(key) && !"".equals(map.get(key))) {
				builder.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
			}
		}
		return builder.append("</xml>").toString();
	}

	/**
	 * 
	 * @Description: TODO
	 * @author myz
	 * @param outTradeNo
	 * @param payFee
	 * @param body
	 * @param randomStr
	 * @param paramMap
	 * 		httpPrefix
	 * 		serverIpDev
	 * 		port
	 * 		wxAppId
	 * 		mchId
	 * 		spbillCreateIp
	 * @return
	 */
    public static Map<String, String> getUnifiedorderMap(String outTradeNo, BigDecimal payFee, String body, String randomStr, HashMap<String, String> paramMap) {
        Map<String, String> map = new HashMap<>();
        String httpPrefix = paramMap.get("httpPrefix");
        String ip = paramMap.get("serverIpDev");
        String port = paramMap.get("port");
        String notifyUrl = Constants.WXPAY_NOTIFY_URL;
    	map.put("appid", paramMap.get("wxAppId"));
        map.put("mch_id", paramMap.get("mchId"));
        map.put("body", body);
        map.put("nonce_str", randomStr);
        map.put("notify_url", httpPrefix.concat(ip).concat(":").concat(port).concat(notifyUrl));
        map.put("out_trade_no", outTradeNo);
        map.put("spbill_create_ip", paramMap.get("spbillCreateIp"));
        map.put("total_fee", String.valueOf(Money.CNYToChinaFen(payFee)));
        map.put("trade_type", Constants.WXPAY_TRADE_TYPE);
        return map;
    }
    
    public static Map<String, String> getUnifiedorderMapH5(String outTradeNo, BigDecimal payFee, String body, String randomStr, HashMap<String, String> paramMap) {
    	Map<String, String> map = new HashMap<>();
    	map.put("appid", paramMap.get("wxAppId"));
    	map.put("mch_id", paramMap.get("mchId"));
    	map.put("body", body);
    	map.put("nonce_str", randomStr);
    	map.put("notify_url", paramMap.get("notifyUrl"));
    	map.put("out_trade_no", outTradeNo);
    	map.put("spbill_create_ip", paramMap.get("spbillCreateIp"));
    	map.put("total_fee", String.valueOf(Money.CNYToChinaFen(payFee)));
    	map.put("trade_type", Constants.WXPAY_TRADE_TYPE_H5);
    	map.put("scene_info", paramMap.get("scene_info"));
    	return map;
    }
    

	public static Map<String, String> getRetMap(String returnCode, String returnMsg, String appId, String mchId,
			String nonceStr, String resultCode, String prepayId, String tradeType, String mwebUrl) {
		Map<String, String> map = new HashMap<>();
		map.put("return_code", returnCode);
		map.put("return_msg", returnMsg);
		map.put("appid", appId);
		map.put("mch_id", mchId);
		map.put("nonce_str", nonceStr);
		map.put("result_code", resultCode);
		map.put("prepay_id", prepayId);
		map.put("trade_type", tradeType);
		if (!StringUtils.isEmpty(mwebUrl)) {
			map.put("mweb_url", mwebUrl);
		}
		return map;
	}

    public static Map<String, String> getRespMap(String appId, String partnerId, String prepayId, String nonceStr, String packageValue, String timeStamp) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("partnerid", partnerId);
        map.put("prepayid", prepayId);
        map.put("noncestr", nonceStr);
        map.put("timestamp", timeStamp);
        map.put("package", packageValue);
        return map;
    }
    
    public static Map<String, String> getRespMap(String appId, String partnerId, String prepayId, String nonceStr, String packageValue, String timeStamp, String mwebUrl) {
    	Map<String, String> map = new HashMap<>();
    	map.put("appid", appId);
    	map.put("partnerid", partnerId);
    	map.put("prepayid", prepayId);
    	map.put("noncestr", nonceStr);
    	map.put("timestamp", timeStamp);
    	map.put("package", packageValue);
    	map.put("mwebUrl", mwebUrl);
    	return map;
    }

    public static Map<String, String> getCallbackMap(String appId, String bankType, Integer cashFee, String feeType, String isSubscribe, String mchId, String nonceStr, String openId, String outTradeNo, String resultCode, String returnCode, String timeEnd, Integer totalFee, String tradeType, String transactionId,Integer couponFee,Integer couponCount,String couponId0,Integer couponFee0) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("bank_type", bankType);
        map.put("cash_fee", cashFee.toString());
        map.put("fee_type", feeType);
        map.put("is_subscribe", isSubscribe);
        map.put("mch_id", mchId);
        map.put("nonce_str", nonceStr);
        map.put("openid", openId);
        map.put("out_trade_no", outTradeNo);
        map.put("result_code", resultCode);
        map.put("return_code", returnCode);
        map.put("time_end", timeEnd);
        map.put("total_fee", totalFee.toString());
        map.put("trade_type", tradeType);
        map.put("transaction_id", transactionId);
        if(null != couponFee){
            map.put("coupon_fee", couponFee.toString());
        }
        if(null != couponCount){
            map.put("coupon_count", couponCount.toString());
        }
        if(null != couponId0){
            map.put("coupon_id_0", couponId0);
        }
        if(null != couponFee0){
            map.put("coupon_fee_0", couponFee0.toString());
        }
        return map;
    }

    /**
     * 
     * @Description: TODO
     * @author myz
     * @param tradeNo
     * @param outRefundNo
     * @param payFee
     * @param feeWx
     * @param randomStr
     * @param paramMap
     * 		wxAppId
     * 		mchId
     * @return
     */
    public static Map<String, String> getRefundMap(String tradeNo, String outRefundNo, BigDecimal payFee, BigDecimal feeWx, String randomStr, HashMap<String, String> paramMap) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", paramMap.get("wxAppId"));
        map.put("mch_id", paramMap.get("mchId"));
        map.put("nonce_str", randomStr);
        map.put("transaction_id", tradeNo);
        map.put("out_refund_no", outRefundNo);
        map.put("total_fee", String.valueOf(Money.CNYToChinaFen(payFee)));
        map.put("refund_fee", String.valueOf(Money.CNYToChinaFen(feeWx)));
        map.put("refund_account",paramMap.get("refundAccount"));
        return map;
    }

	public static Map<String, String> getVerifyRefundMap(String returnCode, String returnMsg, String appId,
			String mchId, String nonceStr, String resultCode, String transactionId, String outTradeNo,
			String outRefundNo, String refundId, Integer refundFee, String refundChannel, Integer couponRefundFee,
			Integer totalFee, Integer cashFee, Integer couponRefundCount, Integer cashRefundFee) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", returnCode);
        map.put("return_msg", returnMsg);
        map.put("appid", appId);
        map.put("mch_id", mchId);
        map.put("nonce_str", nonceStr);
        map.put("result_code", resultCode);
        map.put("transaction_id", transactionId);
        map.put("out_trade_no", outTradeNo);
        map.put("out_refund_no", outRefundNo);
        map.put("refund_id", refundId);
        map.put("refund_fee", refundFee.toString());
        map.put("refund_channel", refundChannel);
        map.put("coupon_refund_fee", couponRefundFee.toString());
        map.put("total_fee", totalFee.toString());
        map.put("cash_fee", cashFee.toString());
        map.put("coupon_refund_count", couponRefundCount.toString());
        map.put("cash_refund_fee", cashRefundFee.toString());
        return map;
    }
    
    /**
     * 
     * @Description: TODO
     * @author myz
     * @param nonceStr
     * @param transactionId
     * @param outTradeNo
     * @param outRefundNo
     * @param refundId
     * @param paramMap
     * 		wxAppId
     * 		mchId
     * @return
     */
	public static Map<String, String> getRefundQueryMap(String nonceStr, String transactionId, String outTradeNo,
			String outRefundNo, String refundId, HashMap<String, String> paramMap) {
		Map<String, String> map = new HashMap<>();
		map.put("appid", paramMap.get("wxAppId"));
		map.put("mch_id", paramMap.get("mchId"));
		map.put("nonce_str", nonceStr);
		map.put("transaction_id", transactionId);
		map.put("out_trade_no", outTradeNo);
		map.put("out_refund_no", outRefundNo);
		map.put("refund_id", refundId);
		return map;
	}
}
