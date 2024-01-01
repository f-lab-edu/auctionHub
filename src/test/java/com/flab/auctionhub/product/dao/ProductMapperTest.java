package com.flab.auctionhub.product.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.domain.UserRoleType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void save() {
        // given
        Product product = getProduct("나이키 슈즈", "사이즈는 280입니다.");

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
        assertThat(result.getCurrentBidPrice()).isEqualTo(0);
        assertThat(result.getStartedAt()).isEqualTo(product.getStartedAt());
        assertThat(result.getEndedAt()).isEqualTo(product.getEndedAt());
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedBy()).isEqualTo(UserRoleType.ADMIN.getValue());
        assertThat(result.getUpdatedAt()).isNull();
        assertThat(result.getUpdatedBy()).isNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getCategoryId()).isEqualTo(1L);
    }

    @Test
    void findAll() {
        // given
        Product product1 = getProduct("나이키 슈즈1", "사이즈는 270입니다.");
        Product product2 = getProduct("나이키 슈즈2", "사이즈는 280입니다.");
        Product product3 = getProduct("나이키 슈즈3", "사이즈는 290입니다.");

        // when


        // then

    }

    @Test
    void findById() {
        // given

        // when


        // then

    }

    @Test
    void update() {
        // given

        // when


        // then

    }

    @Test
    void findAllBySellingStatusIn() {
        // given

        // when


        // then

    }

    private Product getProduct(String name, String description) {
        return Product.builder()
            .name(name)
            .description(description)
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(1000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2023,12,22,05,44,37))
            .endedAt(LocalDateTime.of(2023,12,25,05,44,37))
            .createdBy(UserRoleType.ADMIN.getValue())
            .userId(1L)
            .categoryId(1L)
            .build();
    }
}
