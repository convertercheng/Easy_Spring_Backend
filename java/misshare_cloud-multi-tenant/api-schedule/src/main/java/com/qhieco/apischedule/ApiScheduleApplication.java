package com.qhieco.apischedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"com.qhieco.apiservice", "com.qhieco.config", "com.qhieco.apischedule"})
@EntityScan({"com.qhieco.commonentity"})
@EnableJpaRepositories({"com.qhieco.commonrepo"})
@MapperScan({"com.qhieco.mapper"})
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class ApiScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiScheduleApplication.class, args);
    }
}
