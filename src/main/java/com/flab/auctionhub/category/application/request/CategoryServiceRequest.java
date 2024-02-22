package com.flab.auctionhub.category.application.request;

import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryServiceRequest {

    /**
     * 카테고리 명
     */
    private CategoryType name;

    public Category toEntity(String currentAuditor) {
        return Category.builder()
            .name(this.name)
            .createdBy(currentAuditor)
            .build();
    }
}
