package com.flab.auctionhub.category.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
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
}
