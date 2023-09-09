package com.flab.auctionhub.user.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder // 객체 생성을 편리하게 하기 위한 어노테이션
@Getter // 클래스의 필드에 대한 Getter 메소드를 자동으로 생성해주는 어노테이션
@AllArgsConstructor // 클래스의 코든 필드에 대한 생성자를 자동으로 생성해주는 어노테이션
public class User {

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
    private String roleType;
    /**
     * 휴대폰 번호
     */
    private String phoneNumber;
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
