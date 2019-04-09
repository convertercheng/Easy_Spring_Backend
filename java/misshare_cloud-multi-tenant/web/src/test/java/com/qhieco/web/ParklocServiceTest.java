package com.qhieco.web;

import com.qhieco.webmapper.ParklocMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/9 17:29
 * <p>
 * 类说明：
 * ${说明}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ParklocServiceTest {
    @Autowired
    private ParklocMapper parklocMapper;

    @Test
    public void countByParklocNumberAdParklotIdTest(){
        List<String> numbers = Arrays.asList("享L-A2", "A23");
        log.info(" -------------- " + parklocMapper.countByParklocNumberAdParklotId(numbers, 1010, 4));
    }
}
