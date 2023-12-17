package com.flab.auctionhub.category.application.response;

import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryResponse {

    /**
     * 카테고리 명
     */
    private CategoryType name;

    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
            .name(category.getName())
            .build();
    }
}
