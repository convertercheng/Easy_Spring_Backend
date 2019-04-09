package com.qhieco.apiread;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/7 18:26
 * <p>
 * 类说明：
 *     启动类
 */
@SpringBootApplication
@ComponentScan({"com.qhieco.apiservice", "com.qhieco.apiread", "com.qhieco.config"})
@EntityScan("com.qhieco.commonentity")
@EnableJpaRepositories("com.qhieco.commonrepo")
@EnableAsync
@MapperScan(basePackages= {"com.qhieco.mapper"})
@EnableCaching
@EnableDiscoveryClient
public class ApiReadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiReadApplication.class, args);
	}
}
