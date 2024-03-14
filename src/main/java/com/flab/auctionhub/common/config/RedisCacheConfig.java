package com.flab.auctionhub.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.bid.auction.RedisPubSubListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import java.time.Duration;


@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisCacheConfig {

    private final SimpMessageSendingOperations template;
    private final ObjectMapper mapper;

    @Value("${spring.redis.session.host}") // Spring 프레임워크에서 프로퍼티 값을 주입받을 때 사용하는 어노테이션
    private String host;

    @Value("${spring.redis.cache.port}")
    private int port;


    @Bean
    public RedisConnectionFactory redisCacheConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisCacheTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisCacheConnectionFactory());
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter redisPubsubListenerAdapter() {
        return new MessageListenerAdapter(new RedisPubSubListener(template, mapper));
    }

    @Bean
    public RedisMessageListenerContainer redisPubsubContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisCacheConnectionFactory());
        container.addMessageListener(redisPubsubListenerAdapter(), new PatternTopic("auction"));
        return container;
    }
}
