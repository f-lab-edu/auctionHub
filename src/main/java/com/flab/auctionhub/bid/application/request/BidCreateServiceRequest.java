package com.flab.auctionhub.bid.application.request;

import com.flab.auctionhub.bid.domain.Bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidCreateServiceRequest {

    /**
     * 압찰 가격
     */
    private int price;

    /**
     * 회원 번호
     */
    private Long userId;

    /**
     * 상품 번호
     */
    private Long productId;

    public Bid toEntity() {
        return Bid.builder()
            .price(this.price)
            .userId(this.userId)
            .productId(this.productId)
            .build();
    }
}
