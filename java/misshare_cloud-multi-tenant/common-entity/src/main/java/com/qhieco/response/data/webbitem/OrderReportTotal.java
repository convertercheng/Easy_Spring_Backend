package com.qhieco.response.data.webbitem;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 15:01
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderReportTotal {

    private BigDecimal amount;

    private Integer reserveNum;

    private Integer parkingNum;

    private List<OrderReportData> reserveOrderReportList;

    private List<OrderReportData> parkingOrderReportList;
}
