package com.flab.auctionhub.product.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /**
     * 상품 번호
     */
    private Long id;
    /**
     * 상품 이름
     */
    private String name;
    /**
     * 상품 설명
     */
    private String description;
    /**
     * 판매 상태
     */
    private ProductSellingStatus sellingStatus;
    /**
     * 즉시 구매가
     */
    private int quickPrice;
    /**
     * 시작 입찰 금액
     */
    private int startBidPrice;
    /**
     * 최소 입찰 금액
     */
    private int minBidPrice;
    /**
     * 현재 입찰 금액
     */
    private int currentBidPrice;
    /**
     * 시작 시긴
     */
    private LocalDateTime startedAt;
    /**
     * 마감 시간
     */
    private LocalDateTime endedAt;
    /**
     * 삭제 여부
     */
    private boolean isDeleted;
    /**
     * 등록 일시
     */
    private LocalDateTime createdAt;
    /**
     * 등록자
     */
    private String createdBy;
    /**
     * 수정 일시
     */
    private LocalDateTime updatedAt;
    /**
     * 수정자
     */
    private String updatedBy;
    /**
     * 유저 아이디
     */
    private Long userId;
    /**
     * 카테고리 아이디
     */
    private Long categoryId;
}
