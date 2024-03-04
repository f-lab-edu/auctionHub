package com.flab.auctionhub.order.domain;

import com.flab.auctionhub.order.exception.WrongOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStatusTest {

    @Test
    @DisplayName("주문 상태에 맞는 타입인지 체크한다.")
    void containsOrderStatus() {
        // given
        OrderStatus givenStatus = OrderStatus.INIT;

        // when
        OrderStatus result = OrderStatus.getOrderStatus("주문생성");

        // then
        assertThat(result).isEqualTo(givenStatus);
    }

    @Test
    @DisplayName("주문 상태에 맞는 타입인지 체크한다.")
    void containsOrderStatus2() {
        // given
        OrderStatus givenStatus = OrderStatus.CANCELED;

        // when
        OrderStatus result = OrderStatus.getOrderStatus("주문취소");

        // then
        assertThat(result).isEqualTo(givenStatus);
    }

    @Test
    @DisplayName("주문 상태에 맞는 타입인지 체크한다.")
    void containsOrderStatus3() {
        // given
        OrderStatus givenStatus = OrderStatus.PAYMENT_COMPLETED;

        // when
        OrderStatus result = OrderStatus.getOrderStatus("결제완료");

        // then
        assertThat(result).isEqualTo(givenStatus);
    }

    @Test
    @DisplayName("주문 상태에 맞는 타입인지 체크한다.")
    void containsOrderStatus4() {
        // given
        OrderStatus givenStatus = OrderStatus.PAYMENT_FAILED;

        // when
        OrderStatus result = OrderStatus.getOrderStatus("결제실패");

        // then
        assertThat(result).isEqualTo(givenStatus);
    }

    @Test
    @DisplayName("주문 상태에 맞는 타입인지 체크한다.")
    void containsOrderStatus5() {
        // given
        OrderStatus givenStatus = OrderStatus.RECEIVED;

        // when
        OrderStatus result = OrderStatus.getOrderStatus("주문접수");

        // then
        assertThat(result).isEqualTo(givenStatus);
    }

    @Test
    @DisplayName("주문 상태에 맞는 타입인지 체크한다.")
    void containsOrderStatus6() {
        // given
        OrderStatus givenStatus = OrderStatus.COMPLETED;

        // when
        OrderStatus result = OrderStatus.getOrderStatus("처리완료");

        // then
        assertThat(result).isEqualTo(givenStatus);
    }

    @Test
    @DisplayName("주문 상태에 맞는 타입이 아니라면 예외처리를 한다.")
    void containsOrderStatus7() {
        // when // then
        assertThatThrownBy(() -> OrderStatus.getOrderStatus("X"))
            .isInstanceOf(WrongOrderStatusException.class)
            .hasMessage("잘못된 주문상태 입니다.");
    }
}
