package com.qhieco.apiservice.redis;

import com.qhieco.apiservice.redis.cache.CacheTime;
import com.qhieco.apiservice.redis.cache.CustomizedRedisCacheManager;
import com.qhieco.apiservice.redis.utils.MyStringRedisSerializer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhao.wang
 */
@Configuration
public class RedisConfig {

    /**
     * redisTemplate 配置
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(new MyStringRedisSerializer());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }
    /**
     * 重写RedisCacheManager的getCache方法，实现设置key的有效时间
     * 重写RedisCache的get方法，实现触发式自动刷新
     * <p>
     * 自动刷新方案：
     * 1、获取缓存后再获取一次有效时间，拿这个时间和我们配置的自动刷新时间比较，如果小于这个时间就刷新。
     * 2、每次创建缓存的时候维护一个Map，存放key和方法信息（反射）。当要刷新缓存的时候，根据key获取方法信息。
     * 通过获取其代理对象执行方法，刷新缓存。
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        CustomizedRedisCacheManager redisCacheManager = new CustomizedRedisCacheManager(redisTemplate);
        // 开启使用缓存名称最为key前缀
        redisCacheManager.setUsePrefix(true);
        //这里可以设置一个默认的过期时间 单位是秒
        redisCacheManager.setDefaultExpiration(600L);

        return redisCacheManager;
    }

    /**
     * 显示声明缓存key生成器
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {

        return new SimpleKeyGenerator();
    }

}
