package com.flab.auctionhub.order.application.response;

import com.flab.auctionhub.order.domain.Order;
import com.flab.auctionhub.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {
    /**
     * 주문 번호
     */
    private Long id;
    /**
     * 주문 가격
     */
    private int price;
    /**
     * 주문 상태
     */
    private OrderStatus orderStatus;
    /**
     * 유저 아이디
     */
    private Long userId;
    /**
     * 상품 아이디
     */
    private Long productId;

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .price(order.getPrice())
            .orderStatus(order.getOrderStatus())
            .userId(order.getUserId())
            .productId(order.getProductId())
            .build();
    }
}
