package com.qhieco.web;

import com.qhieco.request.web.OrderScanpayRequest;
import com.qhieco.webservice.OrderParkingScanpayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 17:41
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class OrderParkingScanpayServiceTest {

    @Autowired
    private OrderParkingScanpayService orderParkingScanpayService;

    @Test
    public void queryListTest() {
        OrderScanpayRequest orderScanpayRequest = new OrderScanpayRequest();
        orderScanpayRequest.setSEcho(1);
        orderScanpayRequest.setStart(0);
        orderScanpayRequest.setLength(3);
        log.info(" ---------- " + orderParkingScanpayService.queryList(orderScanpayRequest));
    }
}
