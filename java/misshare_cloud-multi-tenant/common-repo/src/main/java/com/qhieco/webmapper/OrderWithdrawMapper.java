package com.qhieco.webmapper;

import com.qhieco.request.web.OrderWithdrawRequest;
import com.qhieco.response.data.web.OrderWithdrawData;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-30 下午5:00
 * <p>
 * 类说明：
 * ${description}
 */
public interface OrderWithdrawMapper {
    Integer orderCount(OrderWithdrawRequest request);

    List<OrderWithdrawData> orderPage(OrderWithdrawRequest request);

    List<OrderWithdrawData> orderWithdrawExcel(OrderWithdrawRequest request);

}
