package com.qhieco.web;

import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.webmapper.ParklotMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 11:33
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ParklotServiceTest {

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private ParklotRepository parklotRepository;

    @Test
    public void queryParklotLnglatDuplicateByConditionTest(){
        log.info(" ------------ " + parklotMapper.queryParklotLnglatDuplicateByCondition(12, 113.946791d,22.553355));
    }

    @Test
    public void findIdsByNameLikeTest(){
        log.info(" ----------- " + parklotRepository.findIdsByNameLike("dd"));
    }
}
