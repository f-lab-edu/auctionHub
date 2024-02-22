package com.flab.auctionhub.product.domain;

import java.time.LocalDateTime;
import com.flab.auctionhub.common.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product extends BaseEntity {
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
     * 유저 아이디
     */
    private Long userId;
    /**
     * 카테고리 아이디
     */
    private Long categoryId;

    @Builder
    public Product(Long id, String name, String description, ProductSellingStatus sellingStatus,
        int quickPrice, int startBidPrice, int minBidPrice, int currentBidPrice,
        LocalDateTime startedAt, LocalDateTime endedAt, Long userId, Long categoryId,
        boolean isDeleted, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt,
        String updatedBy) {
        super(isDeleted, createdAt, createdBy, updatedAt, updatedBy);
        this.id = id;
        this.name = name;
        this.description = description;
        this.sellingStatus = sellingStatus;
        this.quickPrice = quickPrice;
        this.startBidPrice = startBidPrice;
        this.minBidPrice = minBidPrice;
        this.currentBidPrice = currentBidPrice;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.userId = userId;
        this.categoryId = categoryId;
    }
}

