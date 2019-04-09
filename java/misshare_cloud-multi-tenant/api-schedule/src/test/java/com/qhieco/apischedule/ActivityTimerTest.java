package com.qhieco.apischedule;

import com.qhieco.apischedule.timer.ActivityTimer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/19 11:45
 * <p>
 * 类说明：
 * ${说明}
 */
@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ActivityTimerTest {

    @Autowired
    private ActivityTimer activityTimer;


    @Test
    public void scanPrizeTimeOutTest() {
        activityTimer.scanPrizeTimeOut();
    }
}
