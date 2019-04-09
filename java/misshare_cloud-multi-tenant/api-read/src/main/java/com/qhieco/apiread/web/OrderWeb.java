package com.qhieco.apiread.web;

import com.qhieco.apiservice.OrderService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.*;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.BillListRepData;
import com.qhieco.response.data.api.BillRepData;
import com.qhieco.response.data.api.InvoiceOrderListRepData;
import com.qhieco.response.data.api.InvoiceOrderRepData;
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
 * @version 2.0.1 创建时间: 2018/2/28 16:11
 * <p>
 * 类说明：
 * 订单的controller
 */
@RestController
@Slf4j
@RequestMapping("order")
public class OrderWeb {
    @Autowired
    private OrderService orderService;

    /**
     * 消息预览，展示用户正在使用的订单信息
     *
     * @param request
     * @return
     */
    @PostMapping("/using")
    public Resp queryUsingOrder(@RequestBody OrderQueryRequest request) {
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ORDER_QUERY);
        }
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        return RespUtil.successResp(orderService.queryOrderUsingByUserId(request.getUser_id()));
    }

    /**
     * 账单列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/bill/list")
    public Resp billList(@RequestBody BillQueryRequest request) {
        if (request.getUser_id() == null || request.getPage_num() == null || request.getType() == null) {
            log.error("查询 用户账单列表 方法，传入参数为空");
            throw new QhieException(Status.ApiErr.PARAMS_BILL_QUERY);
        }
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }

        List<BillRepData> billRepData = orderService.queryBillListByUserId(request.getUser_id(), request.getPage_num(), request.getType(), request.getDate());
        BillListRepData listRepData = new BillListRepData();
        listRepData.setBills(billRepData);
        return RespUtil.successResp(listRepData);
    }

    /**
     * 账单详情
     *
     * @return
     */
    @PostMapping(value = "/bill/detail")
    public Resp billDetail(@RequestBody BillDetailQueryRequest request) {
        if (request.getSerial_number() == null || request.getType() == null) {
            log.error("查询 用户账单详情 方法，传入参数为空");
            throw new QhieException(Status.ApiErr.PARAMS_BILL_QUERY);
        }
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        return RespUtil.successResp(orderService.queryBillDetailBySerialNumber(request.getSerial_number(), request.getType(), request.getUser_id()));
    }

    /**
     * 用户可开发票订单列表接口
     *
     * @return
     */
    @PostMapping(value = "/invoice/orderlist")
    public Resp invoiceOrderList(@RequestBody InvoiceOrderListRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_INVOICE_ORDERLIST);
        }
        List<InvoiceOrderRepData> invoiceOrderRepDataList = orderService.queryInvoiceOrderListByCondition(request.getUser_id(), request.getPage_num());
        InvoiceOrderListRepData invoiceOrderListRepData = new InvoiceOrderListRepData();
        invoiceOrderListRepData.setOrders(invoiceOrderRepDataList);
        return RespUtil.successResp(invoiceOrderListRepData);
    }

    /**
     * 查询我的预约列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/reserve/list")
    public Resp reserveOrderList(@RequestBody UserIdPageNumRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_RESERVE_ORDER_LIST);
        }
        return RespUtil.successResp(orderService.queryReserveList(request.getUser_id(), request.getPage_num()));
    }

    /**
     * 我的预约列表 -> 预约详情接口
     *
     * @return 预约订单详情
     */
    @PostMapping(value = "reserve/detail/query")
    public Resp reserveOrderDetail(@RequestBody ReserveOrderDetailRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getOrder_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_RESERVE_ORDER_DETAIL);
        }
        log.info("查询预约订单详情参数，" + request);
        Integer isQuickReserve = request.getIsQuickReserve();
        if (isQuickReserve != null) {
            log.info("快速预约的接口");
            return orderService.queryReserveOrderDetail(request);
        } else {
            return RespUtil.successResp(orderService.queryReserveOrderDetail(request.getOrder_id()));
        }
    }

    /**
     * 订单管理列表/业主、管理员
     *
     * @param request
     * @return
     */
    @PostMapping(value = "list")
    public Resp orderManageList(@RequestBody OrderManageRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(orderService.queryOrderManageList(request.getUser_id(), request.getPage_num()));
    }

    /**
     * 订单管理详情/业主、管理员
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/detail")
    public Resp orderManageDetail(@RequestBody OrderManageDetailRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getOrder_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(orderService.queryOrderManageDetail(request.getUser_id(), request.getOrder_id()));
    }


}
