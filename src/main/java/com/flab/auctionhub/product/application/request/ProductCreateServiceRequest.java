package com.flab.auctionhub.product.application.request;

import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateServiceRequest {

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
     * 시작 시간
     */
    private LocalDateTime startedAt;
    /**
     * 유저 아이디
     */
    private Long userId;
    /**
     * 카테고리 아이디
     */
    private Long categoryId;

    public Product toEntity() {
        return Product.builder()
            .name(this.name)
            .description(this.description)
            .sellingStatus(this.sellingStatus)
            .quickPrice(this.quickPrice)
            .startBidPrice(this.startBidPrice)
            .minBidPrice(this.minBidPrice)
            .startedAt(this.startedAt)
            .endedAt(this.startedAt.plusDays(3))
            .createdBy(this.name)
            .userId(this.userId)
            .categoryId(this.categoryId)
            .build();
    }
}
