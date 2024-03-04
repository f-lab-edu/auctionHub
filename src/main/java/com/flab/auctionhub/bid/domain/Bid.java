package com.flab.auctionhub.bid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Bid {
    /**
     * 입찰 번호
     */
    private Long id;
    /**
     * 입찰 가격
     */
    private int price;
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

    @Builder
    public Bid(Long id, int price, LocalDateTime createdAt, String createdBy, Long userId,
        Long productId) {
        this.id = id;
        this.price = price;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.userId = userId;
        this.productId = productId;
    }
}
