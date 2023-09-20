package com.flab.auctionhub.user.dto.response;

import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserCreateResponse {

    /**
     * 회원 아이디
     */
    private String userId;
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

    public static UserCreateResponse of(User user) {
        return UserCreateResponse.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .roleType(user.getRoleType())
            .phoneNumber(user.getPhoneNumber())
            .build();
    }
}
