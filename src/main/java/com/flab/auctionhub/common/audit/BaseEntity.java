package com.flab.auctionhub.common.audit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    public BaseEntity(boolean isDeleted, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
