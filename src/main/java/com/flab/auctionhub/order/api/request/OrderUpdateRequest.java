package com.flab.auctionhub.order.api.request;

import com.flab.auctionhub.order.application.request.OrderUpdateServiceRequest;
import com.flab.auctionhub.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequest {
    /**
     * 주문 번호
     */
    private Long id;
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

    public OrderUpdateServiceRequest toServiceRequest() {
        return OrderUpdateServiceRequest.builder()
            .id(this.id)
            .orderStatus(this.orderStatus)
            .userId(this.userId)
            .productId(this.productId)
            .build();
    }
}
