package com.qhieco.web.controller;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ApplyInvoiceRequest;
import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.request.web.OrderWithdrawRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.OrderRefundService;
import com.qhieco.webservice.OrderService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/16 上午9:38
 * <p>
 * 类说明：
 * 订单模块接口类
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderWeb {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    private final String order_file_path = "order";

    @PostMapping(value = "pageable")
    public Resp parkingOrder(OrderRequest orderRequest) {
        Resp checkResp = ParamCheck.check(orderRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return orderService.orderPage(orderRequest);
    }

    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id) {
        return orderService.one(id);
    }

    @RequestMapping(value = "excel")
    public void excel(HttpServletResponse response, OrderRequest request) {
        if (ExcelUtil.paramCount(request.getSerialNumber(),request.getParklotName(),request.getPhone(),request.getParkingPhone(),
                request.getParklotType(), request.getPlateNumber(), request.getTradeNo(),request.getStateList(),request.getEndCreateTime(),
                request.getStartCreateTime()) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            orderService.excel(request, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("订单" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "refund/pageable")
    public Resp refundQuery(OrderRefundRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return orderRefundService.refundQuery(request);
    }

    @PostMapping(value = "withdraw/pageable")
    public Resp withdrawQuery(OrderWithdrawRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return orderService.withdrawQuery(request);
    }

    @PostMapping(value = "invoice/pageable")
    public Resp invoiceQuery(ApplyInvoiceRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return orderService.invoiceQuery(request);
    }

    /**
     * 提现订单详情
     * @param id
     * @return
     */
    @GetMapping(value = "withdrawOne/{id}")
    public Resp withdrawOne(@PathVariable Integer id) {
        return orderService.withdrawOne(id);
    }

    /**
     * 关闭提现订单修改状态并且上传文件
     * @param orderWithdrawRequest
     * @return
     */
    @PostMapping(value = "/clseOrderWithdraw")
    public Resp clsedOrderWithdraw(OrderWithdrawRequest orderWithdrawRequest) {
        if(orderWithdrawRequest.getId()==null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return orderService.clsedOrderWithdraw(orderWithdrawRequest);

    }

    /**
     * 提现订单导出
     * @param response
     * @param orderWithdrawRequest
     */
    @RequestMapping(value = "/withdraw/orderWithdrawExcel")
    public void orderWithdrawExcel(HttpServletResponse response,OrderWithdrawRequest orderWithdrawRequest) {
        if (ExcelUtil.paramCount(orderWithdrawRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            orderService.orderWithdrawExcel(orderWithdrawRequest, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("提现申请订单" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 发票详情
     * @param id
     * @return
     */
    @GetMapping(value = "invoiceOne/{id}")
    public Resp invoiceOne(@PathVariable Integer id) {
        return orderService.invoiceOne(id);
    }

    /**
     * 发票申请导出
     * @param response
     * @param request
     */
    @RequestMapping(value = "/invoice/orderInvoiceExcel")
    public void orderInvoiceExcel(HttpServletResponse response,ApplyInvoiceRequest request) {
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            orderService.orderInvoiceExcel(request, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("发票申请信息" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 退款订单导出
     * @param response
     * @param refundRequest
     */
    @RequestMapping(value = "/refund/orderRefundExcel")
    public void orderRefundExcel(HttpServletResponse response,OrderRefundRequest refundRequest) {
        if (ExcelUtil.paramCount(refundRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            orderRefundService.orderRefundExcel(refundRequest,response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("退款订单列表信息" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
