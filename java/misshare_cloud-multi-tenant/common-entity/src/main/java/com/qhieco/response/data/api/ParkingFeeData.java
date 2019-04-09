package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 20:12
 * <p>
 * 类说明：
 * 停车收费规则
 */
@Data
public class ParkingFeeData {

    private Integer id ;
    /**
     * 首小时（元）
     */
    private BigDecimal firstHourFee;
    /**
     * 次小时（元）
     */
    private BigDecimal otherTimeFee;
    /**
     * 封顶（元）
     */
    private BigDecimal maxFee;
    /**
     * 免费时间（分钟）
     */
    private int freeUseTime;
    /**
     * 超时额外收费（元）
     */
    private BigDecimal overTimeFee;
    /**
     * 周末：0，工作日：1
     */
    private int weekday;
}
