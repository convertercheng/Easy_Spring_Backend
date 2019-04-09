package com.qhieco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/15 上午10:19
 * <p>
 * 类说明：
 *     为了解决跨域问题
 */
@Configuration
public class CORSConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("X-Requested-With", "Content-Type")
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                        .allowedOrigins("*")
                        .maxAge(3600)
                        .allowCredentials(true);
            }
        };
    }
}
