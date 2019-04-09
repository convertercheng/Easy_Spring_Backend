package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.LogService;
import com.qhieco.constant.Status;
import com.qhieco.request.api.LogRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 17:43
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class LogServiceTest {
    @Autowired
    private LogService logService;

    @Test
    public void saveLogTest(){
        LogRequest request = new LogRequest();
        request.setContent("sdfsdfsd");
        request.setSource_ip("12.12233");
        request.setSource_model("IPHONE");
        request.setType(Status.LogOperateType.TYPE_LOGIN.getInt());
        request.setUser_id(1);
        logService.saveLog(request);
    }
}
