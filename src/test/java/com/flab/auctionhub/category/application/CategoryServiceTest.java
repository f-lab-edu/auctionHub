package com.flab.auctionhub.category.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.category.api.request.CategoryRequest;
import com.flab.auctionhub.category.application.response.CategoryResponse;
import java.util.List;
import com.flab.auctionhub.category.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @BeforeEach
    void BeforeEach() {
        categoryMapper.save(getCategory(CategoryType.MENSCLOTHING));
        categoryMapper.save(getCategory(CategoryType.WOMENSCLOTHING));
        categoryMapper.save(getCategory(CategoryType.BAG));
        categoryMapper.save(getCategory(CategoryType.SHOES));
        categoryMapper.save(getCategory(CategoryType.HOUSEHOLD));
        categoryMapper.save(getCategory(CategoryType.HEALTH));
    }

    @Test
    @DisplayName("카테고리를 생성하면 카테고리아이디가 리턴된다.")
    void createCategory() {
        // given
        CategoryRequest request = CategoryRequest.builder()
            .name(CategoryType.MENSCLOTHING)
            .build();

        // when
        Long id = categoryService.createCategory(request.toServiceRequest());

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

    @Test
    @DisplayName("카테고리 id값을 이용하여 조회하였을때 존재하지 않다면 예외를 발생한다.")
    void findById() {
        // when // then
        assertThatThrownBy(() -> categoryService.findById(999L))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessage("해당 카테고리를 찾을 수 없습니다.");
    }

    private Category getCategory(CategoryType type) {
        Category category = Category.builder()
            .name(type)
            .build();
        return category;
    }
}
