package com.flab.auctionhub.order.domain;

import com.flab.auctionhub.order.exception.WrongOrderStatusException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    INIT("주문생성"),
    CANCELED("주문취소"),
    PAYMENT_COMPLETED("결제완료"),
    PAYMENT_FAILED("결제실패"),
    RECEIVED("주문접수"),
    COMPLETED("처리완료");

    private final String value;

    public static OrderStatus getOrderStatus(String value) {
        return Arrays.stream(OrderStatus.values()).filter(x -> x.getValue().equals(value))
            .findFirst().orElseThrow(() -> new WrongOrderStatusException("잘못된 주문상태 입니다."));
    }

}
