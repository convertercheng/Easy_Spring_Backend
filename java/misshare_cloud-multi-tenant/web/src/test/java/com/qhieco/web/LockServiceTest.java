package com.qhieco.web;

import com.qhieco.webmapper.LockMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 13:56
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class LockServiceTest {
    @Autowired
    private LockMapper lockMapper;

    @Test
    public void queryNameAdMacDuplicateTest(){
        log.info(" ------------ " + lockMapper.queryNameAdMacDuplicate("CrAM_095B031", "00158D0001095B031", 11));
    }
}
