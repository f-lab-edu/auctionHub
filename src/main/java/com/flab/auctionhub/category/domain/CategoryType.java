package com.flab.auctionhub.category.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.flab.auctionhub.category.exception.WrongCategoryValueException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    MENSCLOTHING("남성의류"),
    WOMENSCLOTHING("여성의류"),
    BAG("가방"),
    SHOES("신발"),
    HOUSEHOLD("생활용품"),
    HEALTH("건강");

    private final String value;

    @JsonCreator
    public static CategoryType getCategoryName(String value) {
        return Arrays.stream(CategoryType.values()).filter(x -> x.getValue().equals(value))
            .findFirst().orElseThrow(() -> new WrongCategoryValueException("잘못된 카테고리 값을 입력하였습니다."));
    }
}
