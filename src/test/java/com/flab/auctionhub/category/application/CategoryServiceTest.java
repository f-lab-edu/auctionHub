package com.flab.auctionhub.category.application;

import static com.flab.auctionhub.util.TestUtils.ACTIVE_PROFILE_TEST;
import static com.flab.auctionhub.util.TestUtils.TEST_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.flab.auctionhub.category.api.request.CategoryRequest;
import com.flab.auctionhub.category.application.response.CategoryResponse;
import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.category.exception.CategoryNotFoundException;
import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles(ACTIVE_PROFILE_TEST)
@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @MockBean
    private LoginUserAuditorAware loginUserAuditorAware;

    @BeforeEach
    void BeforeEach() {
        categoryMapper.save(getCategory(CategoryType.MENSCLOTHING, "admin1"));
        categoryMapper.save(getCategory(CategoryType.WOMENSCLOTHING, "admin2"));
        categoryMapper.save(getCategory(CategoryType.BAG, "admin3"));
        categoryMapper.save(getCategory(CategoryType.SHOES, "admin4"));
        categoryMapper.save(getCategory(CategoryType.HOUSEHOLD, "admin5"));
        categoryMapper.save(getCategory(CategoryType.HEALTH, "admin6"));
    }

    @Test
    @DisplayName("카테고리를 생성하면 카테고리아이디가 리턴된다.")
    void createCategory() {
        // given
        CategoryRequest request = CategoryRequest.builder()
            .name(CategoryType.MENSCLOTHING)
            .build();

        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));

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
    void findCategoryById() {
        // when // then
        assertThatThrownBy(() -> categoryService.findCategoryById(999L))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessage("해당 카테고리를 찾을 수 없습니다.");
    }

    private Category getCategory(CategoryType type, String loginUser) {
        Category category = Category.builder()
            .name(type)
            .createdBy(loginUser)
            .build();
        return category;
    }
}
