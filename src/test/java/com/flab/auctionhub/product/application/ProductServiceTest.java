package com.flab.auctionhub.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.auctionhub.category.exception.CategoryNotFoundException;
import com.flab.auctionhub.product.application.request.ProductCreateServiceRequest;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import java.time.LocalDateTime;
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
        ProductCreateServiceRequest request = getProductCreateServiceRequest("나이키 슈즈", "정품이고 280입니다.", ProductSellingStatus.SELLING);

        // when
        Long productId = productService.createProduct(request);

        // then
        assertThat(productId).isNotNull();
    }

    @Test
    @DisplayName("상품 1개를 존재하지 않은 유저가 등록하려 한다면 예외를 발생한다.")
    void createProductWithoutUser() {
        // given
        ProductCreateServiceRequest request = getProductCreateServiceRequest("나이키 슈즈", "정품이고 280입니다.", ProductSellingStatus.SELLING);

        // when // then
        assertThatThrownBy(() -> productService.createProduct(request))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("상품 1개를 존재하지 않은 카테고리가 등록하려 한다면 예외를 발생한다.")
    void createProductWithoutCategory() {
        // given
        ProductCreateServiceRequest request = getProductCreateServiceRequest("나이키 슈즈", "정품이고 280입니다.", ProductSellingStatus.SELLING);

        // when // then
        assertThatThrownBy(() -> productService.createProduct(request))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessage("해당 카테고리를 찾을 수 없습니다.");
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

    private ProductCreateServiceRequest getProductCreateServiceRequest(String name, String description, ProductSellingStatus status) {
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 280입니다.")
            .sellingStatus(status)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(999L)
            .categoryId(1L)
            .build();
        return request;
    }

}
