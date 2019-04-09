package com.qhieco.apiwrite.web;

import com.qhieco.commonrepo.OrderParkingRepository;
import com.qhieco.mapper.OrderMapper;
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
 * @version 2.0.1 创建时间: 2018/6/22 10:38
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class OrderServiceTest {

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    @Transactional
    public void updateOrderByIdTest() {
        orderParkingRepository.updateOrderById(1, BigDecimal.ZERO, BigDecimal.ONE, "sd", 1L, System.currentTimeMillis());
    }

    @Test
    public void updateEndTimeByIdTest() {
        orderMapper.updateEndTimeById(5678);
    }
}
