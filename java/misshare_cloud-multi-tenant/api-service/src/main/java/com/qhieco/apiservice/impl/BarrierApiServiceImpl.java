package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.BarrierApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 11:01
 * <p>
 * 类说明：
 *      道闸逻辑层
 */
@Service
@Slf4j
public class BarrierApiServiceImpl implements BarrierApiService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> registeredTemp(String jsonContent,String url,String contentType) {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(contentType);
        httpHeaders.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonContent, httpHeaders);
        log.info("register temp request: {}", url + " " + jsonContent);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        log.info("register temp response is {}", response);
        return response;

    }
}

