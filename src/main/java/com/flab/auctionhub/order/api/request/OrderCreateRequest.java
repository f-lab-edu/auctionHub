package com.flab.auctionhub.order.api.request;

import com.flab.auctionhub.order.application.request.OrderCreateServiceRequest;
import com.flab.auctionhub.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    /**
     * 주문 가격
     */
    @Positive(message = "주문 가격은 0원을 초과하여야 합니다.")
    private int price;
    /**
     * 주문 상태
     */
    @NotNull(message = "주문 상태는 필수입니다.")
    private OrderStatus orderStatus;
    /**
     * 유저 아이디
     */
    @NotNull(message = "회원 번호는 필수 입니다.")
    private Long userId;
    /**
     * 상품 아이디
     */
    @NotNull(message = "상품 번호는 필수 입니다.")
    private Long productId;

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
            .price(this.price)
            .orderStatus(this.orderStatus)
            .userId(this.userId)
            .productId(this.productId)
            .build();
    }
}
