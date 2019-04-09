package com.qhieco.trafficmanage;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-22 下午3:10
 * <p>
 * 类说明：
 * ${description}
 */
@SpringBootApplication
@ComponentScan({"com.qhieco.trafficmanage", "com.qhieco.entity"})
@EntityScan("com.qhieco.commonentity")
@EnableJpaRepositories("com.qhieco.commonrepo")
public class TrafficApplication {
    public static void main(String... args){
        SpringApplication.run(TrafficApplication.class, args);
    }
}
