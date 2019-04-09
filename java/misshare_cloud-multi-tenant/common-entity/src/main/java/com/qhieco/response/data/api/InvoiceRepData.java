package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 16:04
 * <p>
 * 类说明：
 * 发票列表相应数据
 */
@lombok.Data
public class InvoiceRepData {
    private Integer invoiceId;
    /**
     * 发票金额
     */
    private BigDecimal fee;

    /**
     * 处理完成时间
     */
    private Long completeTime;

    private Integer state;
}
