package com.qhieco.web.controller;

import com.qhieco.constant.Status;
import com.qhieco.request.web.OrderScanpayRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.OrderParkingScanpayService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 16:09
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController(value = "order/scanpay")
@Slf4j
public class OrderScanpayWeb {

    @Autowired
    private OrderParkingScanpayService orderParkingScanpayService;

    /**
     * 扫码支付订单列表
     */
    @PostMapping(value = "list")
    public Resp queryList(OrderScanpayRequest orderScanpayRequest) {
        log.info("扫码支付订单列表查询参数， orderScanpayRequest= " + orderScanpayRequest);
        Resp checkResp = ParamCheck.check(orderScanpayRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }

        return orderParkingScanpayService.queryList(orderScanpayRequest);
    }
}
