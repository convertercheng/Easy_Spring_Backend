package com.qhieco.webbitem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午11:39
 * <p>
 * 类说明：
 * 启动类
 */
@SpringBootApplication
@ComponentScan({"com.qhieco.bitemservice", "com.qhieco.webbitem", "com.qhieco.util", "com.qhieco.config"})
@MapperScan("com.qhieco.webbitemmapper")
@EntityScan("com.qhieco.commonentity")
@EnableJpaRepositories("com.qhieco.commonrepo")
@ServletComponentScan
@EnableScheduling
public class WebbitemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebbitemApplication.class, args);
    }
}
