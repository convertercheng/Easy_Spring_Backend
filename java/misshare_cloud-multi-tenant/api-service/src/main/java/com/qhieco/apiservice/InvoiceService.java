package com.qhieco.apiservice;

import com.qhieco.response.data.api.InvoiceDetailRepData;
import com.qhieco.response.data.api.InvoiceLastWriteRepData;
import com.qhieco.response.data.api.InvoiceLimitRepData;
import com.qhieco.response.data.api.InvoiceRepData;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 16:03
 * <p>
 * 类说明：
 * 发票service
 */
public interface InvoiceService {

    /**
     * 查询发票记录列表
     *
     * @param userId
     * @param pageNum
     * @return
     */
    List<InvoiceRepData> queryInvoiceRecordListByUserId(Integer userId, int pageNum);

    /**
     * 查询发票详情
     *
     * @param invoiceId
     * @return
     */
    InvoiceDetailRepData queryInvoiceDetailById(Integer invoiceId);

    /**
     * 获取用户上次填写发票信息
     *
     * @param userId
     * @return
     */
    InvoiceLastWriteRepData queryInvoiceLastWriteInfoByUserId(Integer userId);

    /**
     * 查询用户可开发票金额
     * @param userId
     * @return
     */
    InvoiceLimitRepData queryInvoiceAmountByUserId(Integer userId);
}
