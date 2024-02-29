package com.flab.auctionhub.order.application.request;

import com.flab.auctionhub.order.domain.Order;
import com.flab.auctionhub.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateServiceRequest {
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

    public Order toEntity(String currentAuditor) {
        return Order.builder()
            .price(this.price)
            .orderStatus(this.orderStatus)
            .userId(this.userId)
            .productId(this.productId)
            .createdBy(currentAuditor)
            .build();
    }
}
