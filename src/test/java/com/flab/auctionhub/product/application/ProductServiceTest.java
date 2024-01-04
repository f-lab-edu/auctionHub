package com.flab.auctionhub.product.application;

import com.flab.auctionhub.product.application.request.ProductCreateServiceRequest;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserMapper userMapper;


    User user;

    @BeforeEach
    void BeforeEach() {
        user = User.builder()
            .userId("userId")
            .password("testpassword")
            .username("username")
            .phoneNumber("010-0000-0000")
            .createdBy("userId")
            .build();
        userMapper.save(user);
    }

    @Test
    @DisplayName("상품 1개를 등록하면 상품아이디가 리턴된다.")
    void createProduct() {
        // given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 280입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(user.getId())
            .categoryId(1L)
            .build();

        // when
        Long productId = productService.createProduct(request);

        // then
        assertThat(productId).isNotNull();
    }

    @Test
    @DisplayName("상품 1개를 존재하지 않은 유저가 등록하려 한다면 예외를 발생한다.")
    void createProductWithoutUser() {
        // given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 280입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(user.getId())
            .categoryId(1L)
            .build();

        // when
        Long productId = productService.createProduct(request);

        // then
        assertThat(productId).isNotNull();
    }

    @Test
    void getSellingProducts() {
        // given

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
    void findAllProduct() {
        // given

        // when

        // then

    }
}
