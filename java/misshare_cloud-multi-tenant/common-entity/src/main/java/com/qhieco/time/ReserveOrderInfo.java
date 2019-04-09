package com.qhieco.time;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 17:28
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ReserveOrderInfo {
    /**
     * order_parking的id
     */
    private int orderId;
    /**
     * share的id
     */
    private int shareId;
    /**
     * 平台收入
     */
    private BigDecimal platformIncome;
    /**
     * 业主收入
     */
    private BigDecimal ownerIncome;
    /**
     * 停车区收入
     */
    private BigDecimal manageIncome;
}
