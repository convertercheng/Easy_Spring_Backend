package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/16 17:39
 * <p>
 * 类说明：
 * 订单管理详情
 */
@Data
public class OrderManageDetailRespData {

    private Integer orderId;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 车位编号
     */
    private String parklocNumber;
    /**
     * 车场地址
     */
    private String address;
    /**
     * 订单状态
     */
    private Integer state;
    /**
     * 订单编号
     */
    private String serialNumber;
    /**
     * 预约开始时间
     */
    private Long startTime;
    /**
     * 预约结束时间
     */
    private Long endTime;
    /**
     * 进场时间
     */
    private Long enterTime;
    /**
     * 离场时间
     */
    private Long leaveTime;
    /**
     * 车位收入：按订单金额和分成比例，显示车位收入，管理员和业主分成比例不一定相同
     */
    private BigDecimal fee;
}
