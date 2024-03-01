package com.flab.auctionhub.order.application;

import static com.flab.auctionhub.util.TestUtils.ACTIVE_PROFILE_TEST;
import static com.flab.auctionhub.util.TestUtils.TEST_ADMIN;
import static com.flab.auctionhub.util.TestUtils.TEST_SELLER;
import static com.flab.auctionhub.util.TestUtils.TEST_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import com.flab.auctionhub.order.application.request.OrderCreateServiceRequest;
import com.flab.auctionhub.order.application.request.OrderUpdateServiceRequest;
import com.flab.auctionhub.order.application.response.OrderResponse;
import com.flab.auctionhub.order.domain.OrderStatus;
import com.flab.auctionhub.order.exception.OrderNotPossibleException;
import com.flab.auctionhub.product.dao.ProductMapper;
import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
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

@ActiveProfiles(ACTIVE_PROFILE_TEST)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @MockBean
    private LoginUserAuditorAware loginUserAuditorAware;

    User user;

    Product product;
    Product product2;

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

        category = Category.builder()
            .name(CategoryType.MENSCLOTHING)
            .createdBy(TEST_ADMIN)
            .build();
        categoryMapper.save(category);

        product = getProduct("나이키 슈즈", "사이즈 280입니다.", ProductSellingStatus.SUCCESS_BID);
        productMapper.save(product);
        product2 = getProduct("아디다스 슈즈", "사이즈 300입니다.", ProductSellingStatus.SUCCESS_BID);
        productMapper.save(product2);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));

        // when
        Long id = orderService.createOrder(request);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("주문을 생성하는데 상품 가격이랑 주문 가격이 맞지 않으면 예외 처리한다.")
    void createOrderWithMismatchedPrices() {
        // given
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .price(20000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));

        // when // then
        assertThatThrownBy(() -> orderService.createOrder(request))
            .isInstanceOf(OrderNotPossibleException.class)
            .hasMessage("주문 가능한 상품이 아닙니다.");
    }

    @Test
    @DisplayName("주문을 생성하는데 상품 상태가 낙찰된 상태가 아니라면 예외 처리한다.")
    void createOrderWithMismatchedSellingStatus() {
        // given
        product = getProduct("나이키 슈즈", "사이즈 280입니다.", ProductSellingStatus.SELLING);
        productMapper.save(product);
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .price(20000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));

        // when // then
        assertThatThrownBy(() -> orderService.createOrder(request))
            .isInstanceOf(OrderNotPossibleException.class)
            .hasMessage("주문 가능한 상품이 아닙니다.");
    }

    @Test
    @DisplayName("주문 상세 내역을 불러온다.")
    void getOrder() {
        // given
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));
        Long id = orderService.createOrder(request);

        // when
        OrderResponse result = orderService.getOrder(id);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPrice()).isEqualTo(request.getPrice());
        assertThat(result.getOrderStatus()).isEqualTo(request.getOrderStatus());
        assertThat(result.getUserId()).isEqualTo(request.getUserId());
        assertThat(result.getProductId()).isEqualTo(request.getProductId());
    }

    @Test
    @DisplayName("주문 내용을 수정한다.")
    void updateOrder() {
        // given
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));
        Long id = orderService.createOrder(request);

        OrderUpdateServiceRequest updateRequest = OrderUpdateServiceRequest.builder()
            .id(id)
            .orderStatus(OrderStatus.COMPLETED)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        orderService.updateOrder(updateRequest);

        // when
        OrderResponse result = orderService.getOrder(id);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getOrderStatus()).isEqualTo(updateRequest.getOrderStatus());
        assertThat(result.getPrice()).isNotNull();
        assertThat(result.getUserId()).isEqualTo(updateRequest.getUserId());
        assertThat(result.getProductId()).isEqualTo(updateRequest.getProductId());
    }

    @Test
    @DisplayName("유저가 주문한 목록을 불러온다.")
    void findOrdersByUserId() {
        // given
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(TEST_USER));
        orderService.createOrder(request);
        OrderCreateServiceRequest request2 = OrderCreateServiceRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product2.getId())
            .build();
        orderService.createOrder(request2);

        // when
        List<OrderResponse> orderResponseList = orderService.findOrdersByUserId(user.getId());

        // then
        assertThat(orderResponseList).hasSize(2)
            .extracting( "price", "orderStatus", "productId")
            .containsExactlyInAnyOrder(
                tuple(request.getPrice(), request.getOrderStatus(), request.getProductId()),
                tuple(request2.getPrice(), request2.getOrderStatus(), request2.getProductId())
            );
    }

    private Product getProduct(String name, String description, ProductSellingStatus status) {
        return Product.builder()
            .name(name)
            .description(description)
            .sellingStatus(status)
            .quickPrice(50000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .currentBidPrice(30000)
            .startedAt(LocalDateTime.of(2023, 12, 22, 05, 44, 37))
            .endedAt(LocalDateTime.of(2023, 12, 25, 05, 44, 37))
            .createdBy(TEST_SELLER)
            .userId(user.getId())
            .categoryId(category.getId())
            .build();
    }
}
