package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/12 16:54
 * <p>
 * 类说明：
 * 我的预约列表实体
 */
@Data
public class ReserveOrderRespData {

    private Integer orderId;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 订单总额(包括实付金额和优惠券金额)
     */
    private BigDecimal totalFee;
    /**
     * 下单时间
     */
    private Long createTime;
    /**
     * 订单状态
     */
    private Integer state;
}
