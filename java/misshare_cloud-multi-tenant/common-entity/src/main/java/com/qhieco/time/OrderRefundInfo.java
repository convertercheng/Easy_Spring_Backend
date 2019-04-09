package com.qhieco.time;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 17:44
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderRefundInfo {
    /**
     * 退款订单id
     */
    private int refundId;
    /**
     * 停车订单交易号
     */
    private String orderTradeNo;
    /**
     * 退款订单编号
     */
    private String serialNumber;
    /**
     * 订单id
     */
    private int orderId;
    /**
     * 退款金额
     */
    private BigDecimal fee;
    /**
     * 退款渠道
     */
    private int channel;
}
