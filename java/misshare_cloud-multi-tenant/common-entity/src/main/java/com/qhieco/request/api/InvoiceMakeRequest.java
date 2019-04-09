package com.qhieco.request.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 开具电子发票请求参数
 */
@Data
public class InvoiceMakeRequest extends AbstractRequest{
    private Integer user_id;
    private String phone;
    private Integer type;
    private String title;
    private String taxpayerId;
    private String content;
    private String email;
    private BigDecimal amount;
    private String orderIds;
}
