package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 17:02
 * <p>
 * 类说明：
 * 可开发票订单
 */
@lombok.Data
public class InvoiceOrderRepData {
    private Integer orderId;
    /**
     * 订单完成时间
     */
    private Long updateTime;
    /**
     * 实际支付金额
     */
    private BigDecimal fee;
    /**
     * 停车场名称
     */
    private String estateName;

    /**
     * 订单状态
     */
    private Integer state;
}
