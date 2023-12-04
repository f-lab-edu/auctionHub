package com.flab.auctionhub.product.dto.response;

import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

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

    @Builder
    public ProductResponse(Long id, String name, String description, ProductSellingStatus sellingStatus,
        int quickPrice, int startBidPrice, int minBidPrice, int currentBidPrice,
        LocalDateTime startedAt, LocalDateTime endedAt) {
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
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .sellingStatus(product.getSellingStatus())
            .quickPrice(product.getQuickPrice())
            .startBidPrice(product.getStartBidPrice())
            .minBidPrice(product.getMinBidPrice())
            .currentBidPrice(product.getCurrentBidPrice())
            .startedAt(product.getStartedAt())
            .endedAt(product.getEndedAt())
            .build();
    }
}
