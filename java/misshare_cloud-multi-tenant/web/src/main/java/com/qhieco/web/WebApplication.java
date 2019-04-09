package com.qhieco.web;

import com.qhieco.TenantSupport;
import com.qhieco.util.TenantContext;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午11:39
 *          <p>
 *          类说明：
 *          启动类
 */

@SpringBootApplication
@ComponentScan({"com.qhieco.webservice", "com.qhieco.web", "com.qhieco.util", "com.qhieco.config"})
@MapperScan("com.qhieco.webmapper")
@EntityScan("com.qhieco.commonentity")
@EnableJpaRepositories("com.qhieco.commonrepo")
@ServletComponentScan
@EnableScheduling
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


}
