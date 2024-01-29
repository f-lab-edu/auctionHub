package com.flab.auctionhub.category.domain;

import com.flab.auctionhub.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Category extends BaseEntity {

    /**
     * 카테고리 번호
     */
    private Long id;
    /**
     * 카테고리 명
     */
    private CategoryType name;

    @Builder
    public Category(CategoryType name) {
        this.name = name;
    }
}
