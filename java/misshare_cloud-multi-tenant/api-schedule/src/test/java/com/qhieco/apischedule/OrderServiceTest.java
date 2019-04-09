package com.qhieco.apischedule;

import com.qhieco.apiservice.OrderService;
import com.qhieco.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 15:37
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void queryBillDetailBySerialNumberTest(){
        log.info(" ---------- " + orderService.queryBillDetailBySerialNumber("123", 1, 1));
    }

    @Test
    @Transactional
    public void queryInvoiceInfoByOrderIdTest() {
        HashMap<String, Object> invoiceMap = orderMapper.queryInvoiceInfoByOrderId(1);
        log.info("----------" + invoiceMap);
        if (invoiceMap != null && invoiceMap.containsKey("realFee") && invoiceMap.containsKey("userId")) {
//            balanceUserRepository.updateBalanceInvoice(new BigDecimal(invoiceMap.get("realFee").toString()), Integer.valueOf(invoiceMap.get("userId").toString()));
        }
    }

    @Test
    public void queryManageUserIdAdParklotIdByOrderIdTest(){
        log.info(" --------- " + orderMapper.queryManageUserIdAdParklotIdByOrderId(1646));
    }
}
