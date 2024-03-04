package com.flab.auctionhub.order.domain;

import com.flab.auctionhub.common.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Order extends BaseEntity {
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

    @Builder
    public Order(Long id, int price, OrderStatus orderStatus, Long userId, Long productId,
        boolean isDeleted, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt,
        String updatedBy) {
        super(isDeleted, createdAt, createdBy, updatedAt, updatedBy);
        this.id = id;
        this.price = price;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.productId = productId;
    }
}
