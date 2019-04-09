package com.qhieco.apischedule.timer;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/30 15:45
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "test")
public class TestWeb {
    @GetMapping(value = "api")
    public String test(@Param("name") String name) {
        System.out.println(" --------- " + name);
        return "{'d':1}";
    }
}
