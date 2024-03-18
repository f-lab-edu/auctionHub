package com.flab.auctionhub.bid.application.messaging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.bid.dao.BidMapper;
import com.flab.auctionhub.bid.domain.Bid;
import com.flab.auctionhub.bid.exception.BidMessageConversionException;
import com.flab.auctionhub.common.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BidMessageConsumer {

    private final BidMapper bidMapper;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        bidMapper.save(parseMessageToBid(message));
    }

    private Bid parseMessageToBid(String message) {
        try {
            return objectMapper.readValue(message, Bid.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new BidMessageConversionException("입찰 메시지 변환에 실패했습니다.");
    }
}
