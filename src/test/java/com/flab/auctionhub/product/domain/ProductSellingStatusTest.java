package com.flab.auctionhub.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.auctionhub.product.exception.WrongSellingTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductSellingStatusTest {


    @Test
    @DisplayName("지정되지 않은 판매상태 값이 들어오면 예외처리를 한다.")
    void getProductSellingStatus() {
        // when // then
        assertThatThrownBy(() -> ProductSellingStatus.getProductSellingType("지정되지 않은 판매상태"))
            .isInstanceOf(WrongSellingTypeException.class)
            .hasMessage("잘못된 Selling Type을 입력하였습니다.");
    }

    @Test
    @DisplayName("지정된 판매상태 값이 들어오면 ENUM 타입을 반환해준다.")
    void getProductSellingStatus2() {
        // when
        ProductSellingStatus selling = ProductSellingStatus.SELLING;
        ProductSellingStatus hold = ProductSellingStatus.HOLD;
        ProductSellingStatus stopSelling = ProductSellingStatus.STOP_SELLING;

        // then
        assertThat(selling).isEqualTo(ProductSellingStatus.getProductSellingType("판매중"));
        assertThat(hold).isEqualTo(ProductSellingStatus.getProductSellingType("판매대기"));
        assertThat(stopSelling).isEqualTo(ProductSellingStatus.getProductSellingType("판매중지"));
    }
}
