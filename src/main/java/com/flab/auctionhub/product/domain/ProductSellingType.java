package com.flab.auctionhub.product.domain;

import com.flab.auctionhub.product.exception.WrongSellingTypeException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSellingType {
    SELLING("판매중"),
    HOLD("판매보류"),
    STOP_SELLING("판매중지");

    private final String value;

    public static ProductSellingType getProductSellingType(String value) {
        return Arrays.stream(ProductSellingType.values()).filter(x -> x.getValue().equals(value))
            .findFirst().orElseThrow(() -> new WrongSellingTypeException("잘못된 Selling Type을 입력하였습니다."));
    }
}
