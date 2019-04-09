package com.qhieco.webmapper;

import com.qhieco.commonentity.OrderParking;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.OrderParkingData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 9:55
 * <p>
 * 类说明：
 * ${description}
 */
@Mapper
public interface OrderParkingMapper {

    /**
     * 分页显示停车订单列表
     * @param orderRequest
     * @return
     */
    List<OrderParkingData> orderPage(OrderRequest orderRequest);

    /**
     * 查询停车订单列表总记录数
     * @param orderRequest
     * @return
     */
    Integer corderPageTotalCount(OrderRequest orderRequest);
}
