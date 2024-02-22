package com.flab.auctionhub.category.domain;

import com.flab.auctionhub.common.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    public Category(Long id, CategoryType name, boolean isDeleted, LocalDateTime createdAt,
        String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(isDeleted, createdAt, createdBy, updatedAt, updatedBy);
        this.id = id;
        this.name = name;
    }
}
