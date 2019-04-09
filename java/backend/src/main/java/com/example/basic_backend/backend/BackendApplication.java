package com.example.basic_backend.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EntityScan("com.example.basic_backend.backend")
@RestController
@EnableJpaRepositories
@ServletComponentScan
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run( BackendApplication.class, args);
    }

}
