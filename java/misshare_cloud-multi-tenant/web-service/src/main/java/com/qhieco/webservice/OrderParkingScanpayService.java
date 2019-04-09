package com.qhieco.webservice;

import com.qhieco.request.web.OrderScanpayRequest;
import com.qhieco.response.Resp;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 16:10
 * <p>
 * 类说明：
 * ${说明}
 */
public interface OrderParkingScanpayService {

    public Resp queryList(OrderScanpayRequest orderScanpayRequest);
}
