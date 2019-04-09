package com.qhieco.apischedule;

import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.qhieco.apiservice.PayService;
import com.qhieco.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/19 12:35
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PayServiceTest {
    @Autowired
    private PayService payService;

    //(refundId=36, orderTradeNo=4200000064201804191496857955, serialNumber=20419065847840027070, orderId=890, fee=1.00, channel=2)
    @Test
    public void wxRefundQueryTest() {
        String response = payService.wxRefundQuery("4200000149201805149574866005", null,
                "20514650591790028290", null, Constants.PAY_CHANNEL_WXPAY_PUBLIC);
        log.info(" response = " + response);
    }

    @Test
    public void wxpayRefundTest(){
        //生成退款订单号
        String serialNumber = "20531290824030108511";
        log.info( " serialNumber = " + serialNumber);
        String result = payService.wxpayRefund("4200000153201805314885974476", new BigDecimal(3),new BigDecimal(3), serialNumber, 5);
        log.info("微信退款返回参数：" + result);
    }

    @Test
    public void alipayRefundQueryTest() {
        AlipayTradeFastpayRefundQueryResponse alipayTradeFastpayRefundQueryResponse = payService.alipayRefundQuery("2018041721001004970537290806", null,
                "1523959018071");
        log.info(" AlipayTradeFastpayRefundQueryResponse = " + alipayTradeFastpayRefundQueryResponse);
        log.info(alipayTradeFastpayRefundQueryResponse.getBody());
        log.info("支付宝支付退款响应参数，code=" + alipayTradeFastpayRefundQueryResponse.getCode() + ", refundAmount = " +
                alipayTradeFastpayRefundQueryResponse.getRefundAmount() + ", message=" + alipayTradeFastpayRefundQueryResponse.getMsg());

    }

}
