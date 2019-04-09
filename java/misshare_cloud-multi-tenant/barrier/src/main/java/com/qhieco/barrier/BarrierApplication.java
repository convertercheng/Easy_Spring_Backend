package com.qhieco.barrier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/22 下午10:08
 * <p>
 * 类说明：
 *     道闸模块启动类
 */

@SpringBootApplication
@ComponentScan({"com.qhieco.config", "com.qhieco.barrier",})
@EntityScan("com.qhieco.commonentity")
@EnableJpaRepositories("com.qhieco.commonrepo")
@MapperScan(basePackages= {"com.qhieco.mapper"})
public class BarrierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarrierApplication.class, args);
    }
}
