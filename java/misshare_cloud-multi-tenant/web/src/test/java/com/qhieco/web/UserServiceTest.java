package com.qhieco.web;

import com.qhieco.request.web.UserRequest;
import com.qhieco.webmapper.UserMapper;
import com.qhieco.webservice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/1 18:01
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void pageUserTest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setState(0);
        userRequest.setStart(0);
        userRequest.setLength(20);
        userRequest.setSEcho(0);
        log.info(" --------- " + userMapper.pageUser(userRequest));
    }

    @Test
    public void pageUserTotalCountTest() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println(simpleDateFormat.format(new Date()));
        UserRequest userRequest = new UserRequest();
        log.info(" -------------- " + userMapper.pageUserTotalCount(userRequest));
        System.out.println(simpleDateFormat.format(new Date()));
    }
}
