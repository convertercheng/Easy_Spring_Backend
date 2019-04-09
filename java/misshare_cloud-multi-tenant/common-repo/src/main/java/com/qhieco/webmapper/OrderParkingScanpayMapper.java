package com.qhieco.webmapper;

import com.qhieco.request.web.OrderScanpayRequest;
import com.qhieco.response.data.web.OrderScanpayData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 17:13
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface OrderParkingScanpayMapper {

    public List<OrderScanpayData> queryOrderScanpayListByCondition(OrderScanpayRequest orderScanpayRequest);

    public Integer queryCountOrderScanpayListByCondition(OrderScanpayRequest orderScanpayRequest);

}
