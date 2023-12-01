package com.flab.auctionhub.user.domain;

import com.flab.auctionhub.user.exception.WrongRoleCodeException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum UserRoleType {
    ADMIN("A"), SELLER("S"), MEMBER("M");

    private final String value;

    UserRoleType(String value) {
        this.value = value;
    }

    public static UserRoleType getRoleName(String value) {
        return Arrays.stream(UserRoleType.values()).filter(x -> x.getValue().equals(value))
            .findFirst().orElseThrow(() -> new WrongRoleCodeException("잘못된 Role을 입력하였습니다."));
    }
}
