package com.flab.auctionhub.category.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.category.dto.request.CategoryRequest;
import com.flab.auctionhub.category.dto.resoponse.CategoryResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리를 생성하면 카테고리아이디가 리턴된다.")
    void createCategory() {
        // given
        CategoryRequest request = CategoryRequest.builder()
            .name(CategoryType.MENSCLOTHING)
            .build();

        // when
        Long id = categoryService.createCategory(request);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("카테고리 목록 전체를 조회한다.")
    void findAllCategory() {

        // when
        List<CategoryResponse> categoryList = categoryService.findAllCategory();

        // then
        assertThat(categoryList).hasSize(6)
            .extracting("name")
            .containsExactlyInAnyOrder(
                CategoryType.getCategoryName("남성의류"),
                CategoryType.getCategoryName("여성의류"),
                CategoryType.getCategoryName("가방"),
                CategoryType.getCategoryName("신발"),
                CategoryType.getCategoryName("생활용품"),
                CategoryType.getCategoryName("건강")
            );
    }
}
