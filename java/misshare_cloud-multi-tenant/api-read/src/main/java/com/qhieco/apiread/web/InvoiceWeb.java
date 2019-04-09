package com.qhieco.apiread.web;

import com.qhieco.apiservice.InvoiceService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.InvoiceQueryRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.InvoiceListRepData;
import com.qhieco.response.data.api.InvoiceRepData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 16:01
 * <p>
 * 类说明：
 * 用户发票controller
 */
@RestController
@RequestMapping(value = "invoice")
@Slf4j
public class InvoiceWeb {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 查询已开发票记录列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/list/query")
    public Resp queryInvoiceList(@RequestBody InvoiceQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_INVOICE_QUERY);
        }

        List<InvoiceRepData> invoices = invoiceService.queryInvoiceRecordListByUserId(request.getUser_id(), request.getPage_num());
        InvoiceListRepData repData = new InvoiceListRepData();
        repData.setInvoices(invoices);
        return RespUtil.successResp(repData);
    }

    /**
     * 查询发票详情接口
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/detail")
    public Resp queryInvoiceDetail(@RequestBody InvoiceQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getInvoice_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_INVOICE_DETAIL);
        }
        return RespUtil.successResp(invoiceService.queryInvoiceDetailById(request.getInvoice_id()));
    }

    /**
     * 获取用户上次填写发票信息的接口
     *
     * @return
     */
    @PostMapping(value = "/last/info")
    public Resp queryLastInfo(@RequestBody InvoiceQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_INVOICE_QUERY);
        }
        return RespUtil.successResp(invoiceService.queryInvoiceLastWriteInfoByUserId(request.getUser_id()));
    }

    /**
     * 查询用户可开发票金额
     *
     * @param request
     * @return
     */
    @PostMapping(value = "limit")
    public Resp invoiceLimit(@RequestBody InvoiceQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_INVOICE_QUERY);
        }
        return RespUtil.successResp(invoiceService.queryInvoiceAmountByUserId(request.getUser_id()));
    }
}
