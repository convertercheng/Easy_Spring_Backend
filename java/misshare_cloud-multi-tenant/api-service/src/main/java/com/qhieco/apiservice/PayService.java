package com.qhieco.apiservice;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.qhieco.request.api.PayRequest;
import com.qhieco.response.Resp;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:36
 * <p>
 * 类说明：
 * 支付service
 */
public interface PayService {

    /**
     * 支付预约费用
     * @param payRequest
     * @return
     */
    Resp pay(PayRequest payRequest);

    /**
     * 阿里支付回调
     * @param httpServletRequest
     */
    String alipayCallback(HttpServletRequest httpServletRequest) throws AlipayApiException;


    /**
     * 微信支付回调
     * @param httpServletRequest
     */
    String wxpayCallback(HttpServletRequest httpServletRequest);


    /**
     * alipay退款
     * @param tradeNo
     * @param payFee
     * @param refundNo
     * @return
     */
    String alipayTradeRefund(String tradeNo, BigDecimal payFee, String refundNo);


    /**
     * wxpay退款
     * @param tradeNo
     * @param payFee
     * @param fee
     * @param refundNo
     * @return
     */
    String wxpayRefund(String tradeNo, BigDecimal payFee, BigDecimal fee, String refundNo, int payChannel);

    /**
     * 微信退款查询
     * @param outTradeNo
     * @param tradeNo
     * @param refundNo
     * @param outRefundNo
     * @return
     */
    public String wxRefundQuery(String outTradeNo, String tradeNo, String refundNo, String outRefundNo, int payChannel);

    /**
     * 支付宝退款查询
     * @param outTradeNo
     * @param tradeNo
     * @param refundNo
     * @return
     */
    public AlipayTradeFastpayRefundQueryResponse alipayRefundQuery(String outTradeNo, String tradeNo, String refundNo);

    /**
     * 停车场扫码支付停车费用
     * @return
     */
    public Resp wxScanPayParkingFee(String plateNo, Integer parklotId, String unionId, String ip, String openId);

    /**
     * 退款公共入口
     *
     * @param tradeNo      第三方支付流水号
     * @param channel      支付渠道
     * @param realFee      实际支付金额(全额退款)
     * @param serialNumber 退款商户订单号
     * @return
     */
    public String refund(String tradeNo, Integer channel, BigDecimal realFee, String serialNumber);

}
