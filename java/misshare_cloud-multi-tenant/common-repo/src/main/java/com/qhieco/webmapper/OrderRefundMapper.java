package com.qhieco.webmapper;

import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.response.data.web.OrderRefundData;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-30 下午2:43
 * <p>
 * 类说明：
 * ${description}
 */
public interface OrderRefundMapper {
    Integer orderCount(OrderRefundRequest request);

    List<OrderRefundData> orderPage(OrderRefundRequest request);

    List<OrderRefundData> orderRefundExcel(OrderRefundRequest request);

}
