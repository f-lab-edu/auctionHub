package com.flab.auctionhub.user.domain;

import com.flab.auctionhub.common.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Getter // 클래스의 필드에 대한 Getter 메소드를 자동으로 생성해주는 Lombok 어노테이션
@NoArgsConstructor // 해당 클래스에 기본 생성자를 자동으로 생성해주는 기능을 제공하는 Lombok 어노테이션
public class User extends BaseEntity {
    /**
     * 회원 번호
     */
    private Long id;
    /**
     * 회원 아이디
     */
    private String userId;
    /**
     * 회원 비밀번호
     */
    private String password;
    /**
     * 회원명
     */
    private String username;
    /**
     * 역할
     */
    private UserRoleType roleType;
    /**
     * 휴대폰 번호
     */
    private String phoneNumber;

    @Builder
    public User(Long id, String userId, String password, String username, UserRoleType roleType,
        String phoneNumber, boolean isDeleted, LocalDateTime createdAt, String createdBy,
        LocalDateTime updatedAt, String updatedBy) {
        super(isDeleted, createdAt, createdBy, updatedAt, updatedBy);
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.roleType = roleType;
        this.phoneNumber = phoneNumber;
    }

}
