package com.flab.auctionhub.bid.auction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.bid.application.response.BidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPubSubPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper;

    public void convertAndSend(Integer highestBid) {
        try {
            String message = mapper.writeValueAsString(highestBid);
            redisTemplate.convertAndSend("auction", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
