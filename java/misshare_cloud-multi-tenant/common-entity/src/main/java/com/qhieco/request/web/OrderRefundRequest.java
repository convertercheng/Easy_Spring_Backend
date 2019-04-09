package com.qhieco.request.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-30 上午9:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class OrderRefundRequest extends QueryPaged{
    private String phone;
    private BigDecimal feeMax;
    private BigDecimal feeMin;
    private String tradeNo;
    private Integer state;
    private List<Integer> channel;
    private Long startCreateTime;
    private Long endCreateTime;

}
