package com.flab.auctionhub.user.dto.request;

import com.flab.auctionhub.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    /**
     * 회원 아이디
     */
    @NotBlank(message = "아이디 입력은 필수입니다.") // 문자열 값이 null이 아니고 빈문자열이 아닌지 검증하는 어노테이션
    @Size(min = 6, max = 12, message = "아이디는 {min}자리 이상 {max}자 이하여야 합니다.") // 문자열, 컬렉션 등의 크기를 검증하는 어노테이션
    private String userId;
    /**
     * 회원 비밀번호
     */
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 {min}자 이상 {max}자 이하여야 합니다.")
    private String password;
    /**
     * 회원명
     */
    @NotBlank(message = "회원명 입력은 필수입니다.")
    private String username;
    /**
     * 휴대폰 번호
     */
    @NotBlank(message = "휴대폰 번호 입력은 필수입니다.")
    @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식에 맞게 입력해 주세요.") // 문자열 값이 정규 표현식과 일치하는지를 검증하기 위한 어노테이션
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
