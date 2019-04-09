package com.qhieco.apiwrite.web;

import com.qhieco.mapper.ParklotAmountMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/22 17:58
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ParklotAmountServiceTest {

    @Autowired
    private ParklotAmountMapper parklotAmountMapper;

    @Test
    public void queryReservableAmountByParklotIdTest(){
        log.info(" ------------ " + parklotAmountMapper.queryReservableAmountByParklotId(14, 1200000000L, 1200000L,
                System.currentTimeMillis()));
    }
}
