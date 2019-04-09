package com.qhieco.webservice;

import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-9 上午10:32
 * <p>
 * 类说明：
 * ${description}
 */
public interface OrderRefundService {

    /**
     * 退款订单分页查询
     * @param request
     * @return
     */
    Resp refundQuery(OrderRefundRequest request);

    /**
     * 退款申请列表excel导出
     * @param orderRefundRequest
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp orderRefundExcel(OrderRefundRequest orderRefundRequest, OutputStream outputStream)throws IOException;
}
