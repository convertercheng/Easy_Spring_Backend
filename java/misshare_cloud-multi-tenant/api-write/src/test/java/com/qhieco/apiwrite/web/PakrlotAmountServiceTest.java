package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.ParklotAmountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/30 14:10
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PakrlotAmountServiceTest {
    @Autowired
    private ParklotAmountService parklotAmountService;

    @Test
    public void updateParklotAmountInfoByParklotIdTest() {
        parklotAmountService.updateParklotAmountInfoByParklotId(13473, "測試方法");
    }
}
