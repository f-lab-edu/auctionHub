package com.flab.auctionhub.bid.application.response;

import com.flab.auctionhub.bid.domain.Bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {
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

    public static BidResponse of(Bid bid) {
        return BidResponse.builder()
            .price(bid.getPrice())
            .userId(bid.getUserId())
            .productId(bid.getProductId())
            .build();
    }
}
