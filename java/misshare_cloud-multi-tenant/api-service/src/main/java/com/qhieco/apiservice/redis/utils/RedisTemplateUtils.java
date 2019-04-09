package com.qhieco.apiservice.redis.utils;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * 获取默认的RedisTemplate
 *
 * @author yuhao.wang
 */
public final class RedisTemplateUtils {

    private static RedisTemplate redisTemplate;

    public static RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        if (redisTemplate == null) {
            synchronized (RedisTemplateUtils.class) {
                if (redisTemplate == null) {
                    redisTemplate = new RedisTemplate();
                    redisTemplate.setConnectionFactory(redisConnectionFactory);

                    JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
                    redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
                    redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);

                    // 设置键（key）的序列化采用StringRedisSerializer。
                    redisTemplate.setKeySerializer(new MyStringRedisSerializer());
                    redisTemplate.setHashKeySerializer(new MyStringRedisSerializer());
                    redisTemplate.afterPropertiesSet();
                }
            }

        }
        return redisTemplate;
    }
}  