package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.PublishService;
import com.qhieco.mapper.PublishMapper;
import com.qhieco.request.api.PublishCancelRequest;
import com.qhieco.response.data.api.ParklotIdAdParklocIdData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/26 17:43
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PublishServiceTest {

    @Autowired
    private PublishService publishService;

    @Autowired
    private PublishMapper publishMapper;

    @Test
    public void cancelTest() {
        PublishCancelRequest request = new PublishCancelRequest();
        request.setPublishIds("");
        log.info(" ------------------- " + publishService.cancel(request));
    }

    @Test
    public void queryParklocIdAdParklotIdByPublishIdTest() {
        ParklotIdAdParklocIdData parklotIdAdParklocIdData = publishMapper.queryParklocIdAdParklotIdByPublishId(1);
        log.info(" -------------- parklotIdAdParklocIdData =" + parklotIdAdParklocIdData);
    }

}
