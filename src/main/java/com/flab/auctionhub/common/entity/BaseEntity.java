package com.flab.auctionhub.common.entity;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {
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
