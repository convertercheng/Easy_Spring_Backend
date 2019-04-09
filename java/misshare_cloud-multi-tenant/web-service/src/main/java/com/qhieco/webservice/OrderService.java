package com.qhieco.webservice;

import com.qhieco.commonentity.ApplyInvoice;
import com.qhieco.request.web.ApplyInvoiceRequest;
import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.request.web.OrderWithdrawRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/5 下午5:23
 *          <p>
 *          类说明：
 *          ${description}
 */
public interface OrderService {
    /**
     * 查看停车订单详情
     * @param id
     * @return
     */
    Resp one(Integer id);

    /**
     * 停车订单excel导出
     * @param request
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(OrderRequest request, OutputStream outputStream) throws IOException;

    /**
     * 分页查询停车订单信息
     * @return
     */
    Resp orderPage(OrderRequest orderRequest);

//    /**
//     * 退款订单分页查询
//     * @param request
//     * @return
//     */
//    Resp refundQuery(OrderRefundRequest request);

    /**
     * 提现订单分页查询
     * @param request
     * @return
     */
    Resp withdrawQuery(OrderWithdrawRequest request);

    /**
     * 发票申请分页查询
     * @param request
     * @return
     */
    Resp<AbstractPaged<ApplyInvoice>> invoiceQuery(ApplyInvoiceRequest request);

    /**
     * 根据ID查询提现申请详情
     * @param id
     * @return
     */
    Resp withdrawOne(Integer id);


    /**
     * 根据提现ID修改提现订单状态和上传文件
     * @param orderWithdrawRequest
     * @return
     */

    Resp clsedOrderWithdraw(OrderWithdrawRequest orderWithdrawRequest);

    /**
     * 提现提现订单excel导出
     * @param orderWithdrawRequest
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp orderWithdrawExcel(OrderWithdrawRequest orderWithdrawRequest, OutputStream outputStream) throws IOException;

    /**
     * 根据发票订单主键ID查询详情
     * @param id
     * @return
     */
    Resp invoiceOne(Integer id);

    /**
     * 发票申请列表excel导出
     * @param request
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp orderInvoiceExcel(ApplyInvoiceRequest request, OutputStream outputStream) throws IOException;



}
