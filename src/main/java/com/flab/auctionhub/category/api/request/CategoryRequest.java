package com.flab.auctionhub.category.api.request;

import com.flab.auctionhub.category.application.request.CategoryServiceRequest;
import com.flab.auctionhub.category.domain.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    /**
     * 카테고리 명
     */
    @NotNull(message = "카테고리 타입은 필수입니다.")
    private CategoryType name;

    public CategoryServiceRequest toServiceRequest() {
        return CategoryServiceRequest.builder()
            .name(this.name)
            .build();
    }
}
