package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 18:01
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ApplyInvoiceData {

    private Integer id;

    private Integer state;

    private  String phone;

    private Integer type;

    private BigDecimal fee;

    private Long applyTime;

    private Long completeTime;

    private String title;

    private BigDecimal totalFee;

    private  Integer fileId;

    private String path;
}
