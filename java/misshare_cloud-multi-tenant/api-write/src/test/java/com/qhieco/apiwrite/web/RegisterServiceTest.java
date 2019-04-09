package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.RegisterService;
import com.qhieco.request.api.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/19 19:38
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class RegisterServiceTest {
    @Autowired
    private RegisterService registerService;

    @Test
    public void editTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setId(1);
    }
}
