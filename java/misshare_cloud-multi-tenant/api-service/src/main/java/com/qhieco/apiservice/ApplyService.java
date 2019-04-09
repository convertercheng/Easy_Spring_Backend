package com.qhieco.apiservice;

import com.qhieco.commonentity.ApplyInvoice;
import com.qhieco.request.api.InvoiceMakeRequest;
import com.qhieco.request.api.WithdrawRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 14:47
 * <p>
 * 工单申请Service
 */
public interface ApplyService {

    /**
     * 开具发票
     * @param invoiceMakeRequest
     * @return Resp
     */
    Resp makeInvoice(InvoiceMakeRequest invoiceMakeRequest);

    /**
     * 下载发票和发送邮件
     * @param applyInvoice
     * @param pdfUrl
     * @return
     */
    String downLoad(ApplyInvoice applyInvoice, String pdfUrl);


    /**
     * 提现
     * @param withdrawRequest
     * @return
     */
    Resp withdraw(WithdrawRequest withdrawRequest);

}
