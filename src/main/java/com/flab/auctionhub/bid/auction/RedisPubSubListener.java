package com.flab.auctionhub.bid.auction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.bid.application.response.BidResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@RequiredArgsConstructor
public class RedisPubSubListener implements MessageListener {

    private final SimpMessageSendingOperations template;
    private final ObjectMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            BidResponse response = mapper.readValue(message.getBody(), BidResponse.class);
            template.convertAndSend("/topic/bid/" + response.getProductId(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
