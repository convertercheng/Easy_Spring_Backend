package com.qhieco.webservice.impl;

import com.qhieco.constant.Constants;
import com.qhieco.request.web.OrderScanpayRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.OrderScanpayData;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.OrderParkingScanpayMapper;
import com.qhieco.webservice.OrderParkingScanpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 16:12
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class OrderParkingScanpayServiceImpl implements OrderParkingScanpayService {

    @Autowired
    private OrderParkingScanpayMapper orderParkingScanpayMapper;

    @Override
    public Resp queryList(OrderScanpayRequest orderScanpayRequest) {

        int count = orderParkingScanpayMapper.queryCountOrderScanpayListByCondition(orderScanpayRequest);
        List<OrderScanpayData> orderScanpayDataList = null;
        if (count > Constants.EMPTY_CAPACITY) {
            orderScanpayDataList = orderParkingScanpayMapper.queryOrderScanpayListByCondition(orderScanpayRequest);
        }

        AbstractPaged<OrderScanpayData> data = AbstractPaged.<OrderScanpayData>builder()
                .sEcho(orderScanpayRequest.getSEcho() + 1)
                .dataList(orderScanpayDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return RespUtil.successResp(data);
    }

}
