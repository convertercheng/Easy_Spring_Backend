package com.qhieco.response.data.webbitem;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 18:13
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderReportList {
    /**
     * 统计时间 如:2018-07-04
     */
    private String dayOfDate;
    /**
     * 应付停车费
     */
    private BigDecimal parkingFee;
    /**
     * 应收预约费
     */
    private BigDecimal reserveFee;
    /**
     * 停放车辆
     */
    private Integer parkingCount;
}
