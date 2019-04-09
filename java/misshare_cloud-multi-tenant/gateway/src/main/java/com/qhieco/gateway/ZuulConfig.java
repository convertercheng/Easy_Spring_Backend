package com.qhieco.gateway;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-10-12 下午4:57
 * <p>
 * 类说明：
 * ${description}
 */
@Configuration
@EnableZuulProxy
public class ZuulConfig {

    @Bean
    public LocationFilter locationRewriteFilter(){
        return new LocationFilter();
    }
}
