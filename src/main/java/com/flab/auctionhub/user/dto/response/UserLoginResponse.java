package com.flab.auctionhub.user.dto.response;

import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginResponse {

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

    public static UserLoginResponse of(User user) {
        return UserLoginResponse.builder()
            .userId(user.getUserId())
            .password(user.getPassword())
            .username(user.getUsername())
            .roleType(user.getRoleType())
            .phoneNumber(user.getPhoneNumber())
            .build();
    }
}
