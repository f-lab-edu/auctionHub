package com.flab.auctionhub.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderHistory {
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
     * 등록일시
     */
    private LocalDateTime createdAt;
    /**
     * 등록자
     */
    private String createdBy;
    /**
     * 유저 아이디
     */
    private Long userId;
    /**
     * 상품 아이디
     */
    private Long productId;
    /**
     * 주문 아이디
     */
    private Long orderId;

    @Builder
    public OrderHistory(Order order, String currentAuditor) {
        this.price = order.getPrice();
        this.orderStatus = order.getOrderStatus();
        this.createdBy = currentAuditor;
        this.userId = order.getUserId();
        this.productId = order.getProductId();
        this.orderId = order.getId();
    }
}
