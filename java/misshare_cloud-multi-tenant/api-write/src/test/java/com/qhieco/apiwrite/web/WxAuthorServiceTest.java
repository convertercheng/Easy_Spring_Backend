package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.WxAuthorService;
import com.qhieco.commonentity.OrderParkingScanpay;
import com.qhieco.commonrepo.OrderParkingScanpayRepository;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/23 8:53
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class WxAuthorServiceTest {
    @Autowired
    private WxAuthorService wxAuthorService;

    @Autowired
    private OrderParkingScanpayRepository orderParkingScanpayRepository;

    @Test
    public void scanpayAuthorRedirectUrlTest() throws Exception {
        String result = wxAuthorService.scanpayAuthorRedirectUrl("code", "11");
        log.info(" ------------- " + result);
    }

    @Test
    public void findBySerialNumberTest() {

        OrderParkingScanpay orderParkingScanpay = orderParkingScanpayRepository.findBySerialNumber("00525151009710000908");
        log.info(" ----------- " + orderParkingScanpay);
    }

    @Test
    @Transactional
    public void updateInfoTest() {
        orderParkingScanpayRepository.updateInfo(1102, "outTradeNo", BigDecimal.ZERO,
                Status.OrderParking.PAID.getInt(), System.currentTimeMillis());
    }

    @Test
    public void queryPayTimeByPlateNoTest() {
        Long l = orderParkingScanpayRepository.queryPayTimeByPlateNo("1", 1L, 1530691097000L);
        log.info(" ------- " + l);
    }
}
