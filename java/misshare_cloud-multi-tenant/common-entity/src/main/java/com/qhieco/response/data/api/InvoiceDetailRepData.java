package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 16:34
 * <p>
 * 类说明：
 * 发票详情响应实体
 */
@lombok.Data
public class InvoiceDetailRepData {
    private Integer invoiceId;
    /**
     * 处理完成时间
     */
    private Long completeTime;
    private Integer state;
    private String email;
    /**
     * 发票抬头
     */
    private String title;
    /**
     * 纳税人识别号
     */
    private String taxpayerId;
    /**
     * 发票类型
     */
    private Integer type;
    private BigDecimal fee;
    /**
     * 申请时间
     */
    private Long applyTime;
    /**
     * 发票内容
     */
    private String content;
    /**
     * 发票图片路径
     */
    private String filePath;
}
