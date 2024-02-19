package com.flab.auctionhub.bid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
