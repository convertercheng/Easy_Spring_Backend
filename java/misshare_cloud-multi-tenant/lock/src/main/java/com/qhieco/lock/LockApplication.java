package com.qhieco.lock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/28 上午11:04
 * <p>
 * 类说明：
 *     车位锁启动类
 */

@SpringBootApplication
@ComponentScan({"com.qhieco.config", "com.qhieco.lock"})
@EntityScan("com.qhieco.commonentity")
@EnableJpaRepositories("com.qhieco.commonrepo")
@EnableAsync
@MapperScan(basePackages= {"com.qhieco.mapper"})
public class LockApplication {

    @Bean(name = "ribbon")
    @LoadBalanced
    public RestTemplate getRibbonTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(LockApplication.class, args);
    }
}
