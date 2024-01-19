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
public class ProductUpdateServiceRequest {

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
     * 시작 시간
     */
    private LocalDateTime startedAt;

    public Product toEntity() {
        return Product.builder()
            .id(this.id)
            .name(this.name)
            .description(this.description)
            .sellingStatus(this.sellingStatus)
            .quickPrice(this.quickPrice)
            .startBidPrice(this.startBidPrice)
            .minBidPrice(this.minBidPrice)
            .startedAt(this.startedAt)
            .endedAt(this.startedAt.plusDays(3))
            .createdBy(this.name)
            .build();
    }
}
