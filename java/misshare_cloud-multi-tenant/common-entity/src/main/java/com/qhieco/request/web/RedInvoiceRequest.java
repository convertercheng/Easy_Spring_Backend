package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:51
 * <p>
 * 类说明：
 *     车位申请审批请求
 */
@Data
public class RedInvoiceRequest{
    Integer applyId;
    String redReason;
}
