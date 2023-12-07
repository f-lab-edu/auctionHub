package com.flab.auctionhub.category.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Category {

    /**
     * 카테고리 번호
     */
    private Long id;
    /**
     * 카테고리 명
     */
    private CategoryType name;
    /**
     * 삭제여부
     */
    private boolean isDeleted;
    /**
     * 등록일시
     */
    private LocalDateTime createdAt;
    /**
     * 등록자
     */
    private String createdBy;
    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;
    /**
     * 수정자
     */
    private String updatedBy;

    @Builder
    public Category(CategoryType name, boolean isDeleted, LocalDateTime createdAt, String createdBy,
        LocalDateTime updatedAt, String updatedBy) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
