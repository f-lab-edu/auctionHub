package com.flab.auctionhub.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "아이디 입력은 필수입니다.")
    private String userId;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;
}
