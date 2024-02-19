package com.flab.auctionhub.bid.api.request;

import com.flab.auctionhub.bid.application.request.BidCreateServiceRequest;
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
public class BidCreateRequest {

    /**
     * 압찰 가격
     */
    @Positive(message = "입찰 가격은 0원을 초과하여야 합니다.")
    private int price;

    /**
     * 회원 번호
     */
    @NotNull(message = "회원 번호는 필수 입니다.")
    private Long userId;

    /**
     * 상품 번호
     */
    @NotNull(message = "상품 번호는 필수 입니다.")
    private Long productId;

    public BidCreateServiceRequest toServiceRequest() {
        return BidCreateServiceRequest.builder()
            .price(this.price)
            .userId(this.userId)
            .productId(this.productId)
            .build();
    }
}
