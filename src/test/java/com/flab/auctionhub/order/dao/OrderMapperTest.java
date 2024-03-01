package com.flab.auctionhub.order.dao;

import static com.flab.auctionhub.util.TestUtils.ACTIVE_PROFILE_TEST;
import static com.flab.auctionhub.util.TestUtils.TEST_ADMIN;
import static com.flab.auctionhub.util.TestUtils.TEST_SELLER;
import static com.flab.auctionhub.util.TestUtils.TEST_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.order.domain.Order;
import com.flab.auctionhub.order.domain.OrderStatus;
import com.flab.auctionhub.product.dao.ProductMapper;
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
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    User user;

    Product product;

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

        product = Product.builder()
            .name("나이키 슈즈")
            .description("사이즈는 280입니다.")
            .sellingStatus(ProductSellingStatus.SUCCESS_BID)
            .quickPrice(50000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .currentBidPrice(30000)
            .startedAt(LocalDateTime.of(2023,12,22,05,44,37))
            .endedAt(LocalDateTime.of(2023,12,25,05,44,37))
            .createdBy(TEST_SELLER)
            .userId(user.getId())
            .categoryId(category.getId())
            .build();
        productMapper.save(product);
    }

    @Test
    @DisplayName("주문 내용을 생성한다.")
    void save() {
        // given
        Order order = Order.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_SELLER)
            .build();

        // when
        orderMapper.save(order);

        // then
        assertThat(order.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 정보를 불러온다.")
    void findById() {
        // given
        Order order = Order.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_USER)
            .build();
        orderMapper.save(order);

        // when
        Order result = orderMapper.findById(order.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPrice()).isEqualTo(order.getPrice());
        assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(result.getUserId()).isEqualTo(order.getUserId());
        assertThat(result.getProductId()).isEqualTo(order.getProductId());
    }

    @Test
    @DisplayName("주문 정보를 수정한다.")
    void update() {
        // given
        Order order = Order.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_USER)
            .build();
        orderMapper.save(order);
        Order updateOrder = Order.builder()
            .id(order.getId())
            .orderStatus(OrderStatus.COMPLETED)
            .userId(user.getId())
            .productId(product.getId())
            .updatedBy(TEST_USER)
            .build();

        // when
        orderMapper.update(updateOrder);
        Order result = orderMapper.findById(updateOrder.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isEqualTo(updateOrder.getId());
        assertThat(result.getPrice()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(updateOrder.getOrderStatus());
        assertThat(result.getUserId()).isEqualTo(updateOrder.getUserId());
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedBy()).isEqualTo(TEST_USER);
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result.getUpdatedBy()).isEqualTo(TEST_USER);


    }

    @Test
    @DisplayName("회원 번호로 주문 내역들을 불러온다")
    void findAllByUserId() {
        // given
        Order order1 = Order.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_USER)
            .build();
        Order order2 = Order.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_USER)
            .build();

        Order order3 = Order.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_USER)
            .build();
        orderMapper.save(order1);
        orderMapper.save(order2);
        orderMapper.save(order3);

        // when
        List<Order> orderList = orderMapper.findAllByUserId(user.getId());

        // then
        assertThat(orderList).hasSize(3)
            .extracting("price", "orderStatus", "productId")
            .containsExactlyInAnyOrder(
                tuple(order1.getPrice(), order1.getOrderStatus(), order1.getProductId()),
                tuple(order2.getPrice(), order2.getOrderStatus(), order2.getProductId()),
                tuple(order3.getPrice(), order3.getOrderStatus(), order3.getProductId())
            );
    }
}
