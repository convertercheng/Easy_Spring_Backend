package com.qhieco.web;

import com.qhieco.request.web.ParklotDistrictRequest;
import com.qhieco.webservice.ParklotDistrictService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/12 18:31
 * <p>
 * 类说明：
 * ${说明}
 */
@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ParklotDistrictServiceTest {
    @Autowired
    private ParklotDistrictService parklotDistrictService;

    @Test
    public void queryTest() throws Exception {
        ParklotDistrictRequest parklotDistrictRequest = new ParklotDistrictRequest();
        parklotDistrictRequest.setParklotName("美兰");
        parklotDistrictRequest.setLength(10);
        parklotDistrictRequest.setSEcho(1);
        parklotDistrictRequest.setStart(0);
        log.info(" ------------- " + parklotDistrictService.query(parklotDistrictRequest));
    }
}
