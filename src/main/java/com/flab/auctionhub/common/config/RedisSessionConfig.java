package com.flab.auctionhub.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisSessionConfig {
    @Value("${spring.redis.session.host}") // Spring 프레임워크에서 프로퍼티 값을 주입받을 때 사용하는 어노테이션
    private String host;

    @Value("${spring.redis.session.port}")
    private int port;

    @Bean
    @Primary
    public RedisConnectionFactory redisSessionConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisSessionTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisSessionConnectionFactory());
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }
}
