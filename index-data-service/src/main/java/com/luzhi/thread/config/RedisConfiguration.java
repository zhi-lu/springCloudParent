package com.luzhi.thread.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/22
 * redis 配置器
 * Could not autowire. No beans of 'RedisConnectionFactory' type found
 */
@Configuration
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisConfiguration {

    private Duration timeToLive = Duration.ZERO;

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 解决查询缓存转换异常的原因
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // 配置序列化问题(乱码问题)
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
}
