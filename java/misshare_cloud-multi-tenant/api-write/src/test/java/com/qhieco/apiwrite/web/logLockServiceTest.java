package com.qhieco.apiwrite.web;

import com.qhieco.mapper.AccessMapper;
import com.qhieco.mapper.LogLockMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/17 12:47
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class logLockServiceTest {

    @Autowired
    private LogLockMapper logLockMapper;

    @Autowired
    private AccessMapper accessMapper;

    @Test
    public void queryAvgBatteryLimit5Test(){
        log.info(" ----------------- " + logLockMapper.queryAvgBatteryLimit5(124));
    }
//    @Test
//    public void queryLockLogInfoTest(){
//        log.info(" ----------------- " + logLockMapper.queryLockLogInfo(124));
//    }
    @Test
    public void queryCountLogLockByLockIdTest(){
        log.info(" ----------------- " + logLockMapper.queryCountLogLockByLockId(124));
    }
    @Test
    public void testMb(){
        log.info(" ----------------- " + accessMapper.queryAccessListByOrderId(1,1));
    }
}
