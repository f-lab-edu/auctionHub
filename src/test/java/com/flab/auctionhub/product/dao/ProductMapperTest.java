package com.flab.auctionhub.product.dao;

import static com.flab.auctionhub.product.domain.ProductSellingStatus.HOLD;
import static com.flab.auctionhub.product.domain.ProductSellingStatus.SELLING;
import static com.flab.auctionhub.product.domain.ProductSellingStatus.SOLD_OUT;
import static com.flab.auctionhub.util.TestUtils.ACTIVE_PROFILE_TEST;
import static com.flab.auctionhub.util.TestUtils.TEST_ADMIN;
import static com.flab.auctionhub.util.TestUtils.TEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import java.time.LocalDateTime;
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
class ProductMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    User user;

    Category category;

    @BeforeEach
    void BeforeEach() {
        user = User.builder()
            .userId("userId")
            .password("testpassword")
            .username("username")
            .phoneNumber("010-0000-0000")
            .build();
        userMapper.save(user);
        category = getCategory(CategoryType.MENSCLOTHING);
        categoryMapper.save(category);
    }

    @Test
    @DisplayName("상품 등록을 테스트한다.")
    void save() {
        // given
        Product product = getProduct("나이키 슈즈", "사이즈는 280입니다.", SELLING);

        // when
        productMapper.save(product);
        Product result = productMapper.findById(product.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getSellingStatus()).isEqualTo(product.getSellingStatus());
        assertThat(result.getQuickPrice()).isEqualTo(product.getQuickPrice());
        assertThat(result.getStartBidPrice()).isEqualTo(product.getStartBidPrice());
        assertThat(result.getMinBidPrice()).isEqualTo(product.getMinBidPrice());
        assertThat(result.getCurrentBidPrice()).isZero();
        assertThat(result.getStartedAt()).isEqualTo(product.getStartedAt());
        assertThat(result.getEndedAt()).isEqualTo(product.getEndedAt());
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedBy()).isNotNull();
        assertThat(result.getUpdatedAt()).isNull();
        assertThat(result.getUpdatedBy()).isNull();
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getCategoryId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("여러 상품을 저장하고 전체 상품을 조회한다.")
    void findAll() {
        // given
        Product product1 = getProduct("나이키 슈즈1", "사이즈는 270입니다.", SELLING);
        Product product2 = getProduct("나이키 슈즈2", "사이즈는 280입니다.", HOLD);
        Product product3 = getProduct("나이키 슈즈3", "사이즈는 290입니다.", SOLD_OUT);
        productMapper.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> productList = productMapper.findAll();

        // then
        assertThat(productList).hasSize(3)
            .extracting("name", "description", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple(product1.getName(), product1.getDescription(), product1.getSellingStatus()),
                tuple(product2.getName(), product2.getDescription(), product2.getSellingStatus()),
                tuple(product3.getName(), product3.getDescription(), product3.getSellingStatus())
            );
    }

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void update() {
        // given
        Product product = getProduct("나이키 슈즈1", "사이즈는 280입니다.", SELLING);
        productMapper.save(product);
        Product updateProduct = Product.builder()
            .id(product.getId())
            .name("나이키 슈즈2")
            .description("사이즈는 290입니다.")
            .sellingStatus(SOLD_OUT)
            .quickPrice(1000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2023,12,22,05,44,37))
            .endedAt(LocalDateTime.of(2023,12,25,05,44,37))
            .updatedBy(TEST_SELLER)
            .userId(user.getId())
            .categoryId(category.getId())
            .build();

        // when
        productMapper.update(updateProduct);

        Product result = productMapper.findById(product.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(updateProduct.getName());
        assertThat(result.getDescription()).isEqualTo(updateProduct.getDescription());
        assertThat(result.getSellingStatus()).isEqualTo(updateProduct.getSellingStatus());
        assertThat(result.getQuickPrice()).isEqualTo(updateProduct.getQuickPrice());
        assertThat(result.getStartBidPrice()).isEqualTo(updateProduct.getStartBidPrice());
        assertThat(result.getMinBidPrice()).isEqualTo(updateProduct.getMinBidPrice());
        assertThat(result.getCurrentBidPrice()).isZero();
        assertThat(result.getStartedAt()).isEqualTo(updateProduct.getStartedAt());
        assertThat(result.getEndedAt()).isEqualTo(updateProduct.getEndedAt());
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedBy()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result.getUpdatedBy()).isEqualTo(TEST_SELLER);
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getCategoryId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("판매중, 판매대기인 상품을 조회한다.")
    void findAllBySellingStatusIn() {
        // given
        Product product1 = getProduct("나이키 슈즈1", "사이즈는 270입니다.", SELLING);
        Product product2 = getProduct("나이키 슈즈2", "사이즈는 280입니다.", HOLD);
        Product product3 = getProduct("나이키 슈즈3", "사이즈는 290입니다.", SOLD_OUT);
        productMapper.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> productSellingHoldList = productMapper.findAllBySellingStatusIn(
            List.of(SELLING, HOLD));

        // then
        assertThat(productSellingHoldList).hasSize(2)
            .extracting("name", "description", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple(product1.getName(), product1.getDescription(), product1.getSellingStatus()),
                tuple(product2.getName(), product2.getDescription(), product2.getSellingStatus())
            );
    }

    private Product getProduct(String name, String description, ProductSellingStatus status) {
        return Product.builder()
            .name(name)
            .description(description)
            .sellingStatus(status)
            .quickPrice(1000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2023,12,22,05,44,37))
            .endedAt(LocalDateTime.of(2023,12,25,05,44,37))
            .userId(user.getId())
            .categoryId(category.getId())
            .createdBy(TEST_SELLER)
            .build();
    }

    private Category getCategory(CategoryType type) {
        Category category = Category.builder()
            .name(type)
            .createdBy(TEST_ADMIN)
            .build();
        return category;
    }
}
