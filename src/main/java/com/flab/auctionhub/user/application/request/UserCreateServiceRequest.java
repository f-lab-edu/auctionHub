package com.flab.auctionhub.user.application.request;

import com.flab.auctionhub.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateServiceRequest {

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
     * 휴대폰 번호
     */
    private String phoneNumber;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
            .userId(this.getUserId())
            .password(passwordEncoder.encode(this.getPassword()))
            .username(this.getUsername())
            .phoneNumber(this.getPhoneNumber())
            .createdBy(this.getUsername())
            .build();
    }
}
