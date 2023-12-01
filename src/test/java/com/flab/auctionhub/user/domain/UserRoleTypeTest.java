package com.flab.auctionhub.user.domain;

import com.flab.auctionhub.user.exception.WrongRoleCodeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRoleTypeTest {

    @Test
    @DisplayName("회원 타입에 맞는 역할 타입인지 체크한다.")
    void containsUserType() {
        // given
        UserRoleType givenType = UserRoleType.MEMBER;

        // when
        UserRoleType result = UserRoleType.getRoleName("M");

        // then
        Assertions.assertThat(result).isEqualTo(givenType);
    }

    @Test
    @DisplayName("회원 타입에 맞는 역할 타입인지 체크한다.")
    void containsUserType2() {
        // given
        UserRoleType givenType = UserRoleType.SELLER;

        // when
        UserRoleType result = UserRoleType.getRoleName("S");

        // then
        Assertions.assertThat(result).isEqualTo(givenType);
    }

    @Test
    @DisplayName("회원 타입에 맞는 역할 타입인지 체크한다.")
    void containsUserType3() {
        // given
        UserRoleType givenType = UserRoleType.ADMIN;

        // when
        UserRoleType result = UserRoleType.getRoleName("A");

        // then
        Assertions.assertThat(result).isEqualTo(givenType);
    }

    @Test
    @DisplayName("회원 타입에 맞는 역할 타입이 아니라면 예외처리를 한다.")
    void containsUserType4() {
        // when // then
        Assertions.assertThatThrownBy(() -> UserRoleType.getRoleName("X"))
            .isInstanceOf(WrongRoleCodeException.class)
            .hasMessage("잘못된 Role을 입력하였습니다.");
    }
}
