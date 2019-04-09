package com.qhieco.response.data.webbitem;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 14:26
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderReportData {

    private String dayOfDate;

    private Integer plateCount;

    private BigDecimal manageIncome;

    public OrderReportData() {
    }

    public OrderReportData(String dayOfDate, Integer plateCount, BigDecimal manageIncome) {
        this.dayOfDate = dayOfDate;
        this.plateCount = plateCount;
        this.manageIncome = manageIncome;
    }
}
