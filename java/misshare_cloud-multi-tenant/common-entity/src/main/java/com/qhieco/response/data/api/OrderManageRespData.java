package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/16 16:04
 * <p>
 * 类说明：
 * 订单管理列表
 */
@Data
public class OrderManageRespData {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 下单时间
     */
    private Long createTime;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 车位编号
     */
    private String parklocNumber;
    /**
     * 订单状态，包括：未支付，进行中、已完成、已取消、超时取消。
     */
    private Integer state;

    /**
     * 订单金额
     */
    private BigDecimal fee;
}
