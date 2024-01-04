package com.flab.auctionhub.category.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.user.domain.UserRoleType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CategoryMapperTest {

    @Autowired
    CategoryMapper categoryMapper;

    @Test
    @DisplayName("카테고리를 생성한다.")
    void save() {
        // given
        Category category = Category.builder()
            .name(CategoryType.MENSCLOTHING)
            .createdBy(UserRoleType.ADMIN.getValue())
            .build();

        // when
        categoryMapper.save(category);

        // then
        assertThat(category.getId()).isNotNull();
    }


    @Test
    @DisplayName("카테고리 목록을 전체 조회한다.")
    void findAll() {
        // when
        List<Category> categoryList = categoryMapper.findAll();

        // then
        assertThat(categoryList).hasSize(6)
            .extracting("name", "isDeleted", "createdBy")
            .containsExactlyInAnyOrder(
                tuple(CategoryType.getCategoryName("남성의류"), false, "A"),
                tuple(CategoryType.getCategoryName("여성의류"), false, "A"),
                tuple(CategoryType.getCategoryName("가방"), false, "A"),
                tuple(CategoryType.getCategoryName("신발"), false, "A"),
                tuple(CategoryType.getCategoryName("생활용품"), false, "A"),
                tuple(CategoryType.getCategoryName("건강"), false, "A")
            );
    }
}
