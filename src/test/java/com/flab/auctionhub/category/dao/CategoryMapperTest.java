package com.flab.auctionhub.category.dao;

import static com.flab.auctionhub.util.TestUtils.ACTIVE_PROFILE_TEST;
import static com.flab.auctionhub.util.TestUtils.TEST_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles(ACTIVE_PROFILE_TEST)
@Transactional
@SpringBootTest
class CategoryMapperTest {

    @Autowired
    CategoryMapper categoryMapper;

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
    @DisplayName("카테고리를 생성한다.")
    void save() {
        // given
        Category category = Category.builder()
            .name(CategoryType.MENSCLOTHING)
            .createdBy(TEST_ADMIN)
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
                tuple(CategoryType.getCategoryName("남성의류"), false, "admin1"),
                tuple(CategoryType.getCategoryName("여성의류"), false, "admin2"),
                tuple(CategoryType.getCategoryName("가방"), false, "admin3"),
                tuple(CategoryType.getCategoryName("신발"), false, "admin4"),
                tuple(CategoryType.getCategoryName("생활용품"), false, "admin5"),
                tuple(CategoryType.getCategoryName("건강"), false, "admin6")
            );
    }
    private Category getCategory(CategoryType type, String loginUser) {
        Category category = Category.builder()
            .name(type)
            .createdBy(loginUser)
            .build();
        return category;
    }

}
