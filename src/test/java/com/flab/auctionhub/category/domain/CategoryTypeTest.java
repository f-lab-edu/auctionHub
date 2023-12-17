package com.flab.auctionhub.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.auctionhub.category.exception.WrongCategoryValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTypeTest {

    @Test
    @DisplayName("지정되지 않은 카테고리 타입이 아닌 값이 들어오면 예외처리를 한다.")
    void getCategoryName() {
        // when // then
        assertThatThrownBy(() -> CategoryType.getCategoryName("지정되지 않은 카테고리"))
            .isInstanceOf(WrongCategoryValueException.class)
            .hasMessage("잘못된 카테고리 값을 입력하였습니다.");
    }

    @Test
    @DisplayName("지정된 카테고리 타입 값이 들어오면 ENUM 타입을 반환해준다.")
    void getCategoryName2() {
        // when
        CategoryType categoryName = CategoryType.getCategoryName("남성의류");

        // then
        assertThat(categoryName).isEqualTo(CategoryType.MENSCLOTHING);
    }
}
