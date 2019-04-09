package com.qhieco.juhe;

import com.qhieco.interceptor.LoggingRequestInterceptor;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午2:34
 * <p>
 * 类说明：
 *    RestTemplate单例
 */

public class SingletonRestTemplate {

    private RestTemplate restTemplate;

    private SingletonRestTemplate(){
        restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
    }

    private static class SingletonRestTemplateHolder {
        private static final SingletonRestTemplate INSTANCE = new SingletonRestTemplate();
    }

    public static SingletonRestTemplate getInstance() {
        return SingletonRestTemplateHolder.INSTANCE;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
