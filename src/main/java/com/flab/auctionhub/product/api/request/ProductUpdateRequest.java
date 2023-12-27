package com.flab.auctionhub.product.api.request;

import com.flab.auctionhub.product.application.request.ProductUpdateServiceRequest;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {

    /**
     * 상품 번호
     */
    @NotNull
    private Long id;
    /**
     * 상품 이름
     */
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;
    /**
     * 상품 설명
     */
    private String description;
    /**
     * 판매 상태
     */
    @NotNull(message = "상품 상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;
    /**
     * 즉시 구매가
     */
    @Positive(message = "즉시 구매가는 0원을 초과하여야 합니다.")
    private int quickPrice;
    /**
     * 시작 입찰 금액
     */
    @Positive(message = "시작 입착액은 0원을 초과하여야 합니다.")
    private int startBidPrice;
    /**
     * 최소 입찰 금액
     */
    @Positive(message = "최소 입착액은 0원을 초과하여야 합니다.")
    private int minBidPrice;
    /**
     * 시작 시간
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:MM:SS")
    private LocalDateTime startedAt;

    public ProductUpdateServiceRequest toServiceRequest() {
        return ProductUpdateServiceRequest.builder()
            .id(this.id)
            .name(this.name)
            .description(this.description)
            .sellingStatus(this.sellingStatus)
            .quickPrice(this.quickPrice)
            .startBidPrice(this.startBidPrice)
            .minBidPrice(this.minBidPrice)
            .startedAt(this.startedAt)
            .build();
    }

}
