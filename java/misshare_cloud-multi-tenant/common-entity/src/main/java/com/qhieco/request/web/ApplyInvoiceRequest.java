package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-2 上午10:35
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ApplyInvoiceRequest extends  QueryPaged {
    private String phone;
    private String title;
    private Long startApplyTime;
    private Long endApplyTime;
}

