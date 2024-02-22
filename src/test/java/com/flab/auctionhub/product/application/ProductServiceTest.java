package com.flab.auctionhub.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.category.exception.CategoryNotFoundException;
import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import com.flab.auctionhub.product.application.request.ProductCreateServiceRequest;
import com.flab.auctionhub.product.application.request.ProductUpdateServiceRequest;
import com.flab.auctionhub.product.application.response.ProductResponse;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import java.time.LocalDateTime;
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

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @MockBean
    private LoginUserAuditorAware loginUserAuditorAware;

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
    @DisplayName("상품 1개를 등록하면 상품아이디가 리턴된다.")
    void createProduct() {
        // given
        ProductCreateServiceRequest request = getProductCreateServiceRequest("나이키 슈즈", "정품이고 280입니다.", ProductSellingStatus.SELLING);
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("seller"));

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
            .sellingStatus( ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(9999L)
            .categoryId(category.getId())
            .build();

        // when // then
        assertThatThrownBy(() -> productService.createProduct(request))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("상품 1개를 존재하지 않은 카테고리가 등록하려 한다면 예외를 발생한다.")
    void createProductWithoutCategory() {
        // given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 280입니다.")
            .sellingStatus( ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(user.getId())
            .categoryId(9999L)
            .build();

        // when // then
        assertThatThrownBy(() -> productService.createProduct(request))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessage("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("판매중, 판매대기인 상품을 조회한다.")
    void getSellingProducts() {
        // given
        ProductCreateServiceRequest request1 = getProductCreateServiceRequest("나이키 슈즈1", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        ProductCreateServiceRequest request2 = getProductCreateServiceRequest("나이키 슈즈2", "정품이고 280입니다.", ProductSellingStatus.STOP_SELLING);
        ProductCreateServiceRequest request3 = getProductCreateServiceRequest("나이키 슈즈3", "정품이고 290입니다.", ProductSellingStatus.HOLD);
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("seller"));
        productService.createProducts(List.of(request1, request2, request3));

        // when
        List<ProductResponse> productResponseList = productService.getSellingProducts();

        // then
        assertThat(productResponseList).hasSize(2)
            .extracting("name", "description", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("나이키 슈즈1", "정품이고 270입니다.", ProductSellingStatus.SELLING),
                tuple("나이키 슈즈3", "정품이고 290입니다.", ProductSellingStatus.HOLD)
            );

    }

    @Test
    @DisplayName("상품 id로 상품을 조회한다.")
    void findById() {
        // given
        ProductCreateServiceRequest request = getProductCreateServiceRequest("나이키 슈즈", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("seller"));
        Long productId = productService.createProduct(request);

        // when
        ProductResponse productResponse = productService.findById(productId);

        // then
        assertThat(productResponse.getId()).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(productResponse.getName());
        assertThat(productResponse.getDescription()).isEqualTo(productResponse.getDescription());
        assertThat(productResponse.getSellingStatus()).isEqualTo(productResponse.getSellingStatus());
        assertThat(productResponse.getQuickPrice()).isEqualTo(productResponse.getQuickPrice());
        assertThat(productResponse.getStartBidPrice()).isEqualTo(productResponse.getStartBidPrice());
        assertThat(productResponse.getMinBidPrice()).isEqualTo(productResponse.getMinBidPrice());
        assertThat(productResponse.getCurrentBidPrice()).isEqualTo(productResponse.getCurrentBidPrice());
        assertThat(productResponse.getStartedAt()).isEqualTo(productResponse.getStartedAt());
        assertThat(productResponse.getEndedAt()).isEqualTo(productResponse.getEndedAt());
    }

    @Test
    @DisplayName("상품을 수정한다.")
    void update() {
        // given
        ProductCreateServiceRequest request1 = getProductCreateServiceRequest("나이키 슈즈", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("seller"));
        Long productId = productService.createProduct(request1);

        ProductUpdateServiceRequest request2 = ProductUpdateServiceRequest.builder()
            .id(productId)
            .name("아이다스 슈즈")
            .description("아디다스 정품")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(30000)
            .startBidPrice(2000)
            .minBidPrice(2000)
            .startedAt(LocalDateTime.of(2013,12,30,05,33,35))
            .userId(user.getId())
            .categoryId(category.getId())
            .build();
        productService.update(request2);

        // when
        ProductResponse productResponse = productService.findById(productId);

        // then
        assertThat(productResponse.getId()).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(productResponse.getName());
        assertThat(productResponse.getDescription()).isEqualTo(productResponse.getDescription());
        assertThat(productResponse.getSellingStatus()).isEqualTo(productResponse.getSellingStatus());
        assertThat(productResponse.getQuickPrice()).isEqualTo(productResponse.getQuickPrice());
        assertThat(productResponse.getStartBidPrice()).isEqualTo(productResponse.getStartBidPrice());
        assertThat(productResponse.getMinBidPrice()).isEqualTo(productResponse.getMinBidPrice());
        assertThat(productResponse.getCurrentBidPrice()).isEqualTo(productResponse.getCurrentBidPrice());
        assertThat(productResponse.getStartedAt()).isEqualTo(productResponse.getStartedAt());
        assertThat(productResponse.getEndedAt()).isEqualTo(productResponse.getEndedAt());
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void findAllProduct() {
        // given
        ProductCreateServiceRequest request1 = getProductCreateServiceRequest("나이키 슈즈1", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        ProductCreateServiceRequest request2 = getProductCreateServiceRequest("나이키 슈즈2", "정품이고 280입니다.", ProductSellingStatus.STOP_SELLING);
        ProductCreateServiceRequest request3 = getProductCreateServiceRequest("나이키 슈즈3", "정품이고 290입니다.", ProductSellingStatus.HOLD);
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("seller"));
        productService.createProducts(List.of(request1, request2, request3));

        // when
        List<ProductResponse> productResponseList = productService.findAllProduct();

        // then
        assertThat(productResponseList).hasSize(3)
            .extracting("name", "description", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple(request1.getName(), request1.getDescription(), ProductSellingStatus.SELLING),
                tuple(request2.getName(), request2.getDescription(), ProductSellingStatus.STOP_SELLING),
                tuple(request3.getName(), request3.getDescription(), ProductSellingStatus.HOLD)
            );
    }

    private ProductCreateServiceRequest getProductCreateServiceRequest(String name, String description, ProductSellingStatus status) {
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .name(name)
            .description(description)
            .sellingStatus(status)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(user.getId())
            .categoryId(category.getId())
            .build();
        return request;
    }

    private Category getCategory(CategoryType type) {
        Category category = Category.builder()
            .name(type)
            .createdBy("admin")
            .build();
        return category;
    }
}
