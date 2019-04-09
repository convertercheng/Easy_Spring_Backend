package com.qhieco.eureka.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-7-10 下午4:21
 * <p>
 * 类说明：
 * ${description}
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaServerApplication.class)
                .web(true).run(args);
    }
}
