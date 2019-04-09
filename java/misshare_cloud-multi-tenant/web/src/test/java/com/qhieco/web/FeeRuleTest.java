package com.qhieco.web;

import com.qhieco.commonentity.OrderParking;
import com.qhieco.webservice.FeeRuleService;
import com.qhieco.webservice.impl.FeeRuleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-5-28 下午3:55
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FeeRuleTest {
    @Autowired
    FeeRuleService feeRuleService;

    @Test
    public void test(){
        OrderParking orderParking = new OrderParking();
        orderParking.setParklotId(11);
        //10
        orderParking.setStartTime(1527472800000L);
        //14
        orderParking.setEndTime(1527487100000L);
        orderParking.setRealStartTime(1527472800000L);
        feeRuleService.calculateFee(orderParking);
    }
}
