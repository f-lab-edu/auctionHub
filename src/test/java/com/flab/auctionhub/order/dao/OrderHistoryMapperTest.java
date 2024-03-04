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
import com.flab.auctionhub.order.domain.OrderHistory;
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
class OrderHistoryMapperTest {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderHistoryMapper orderHistoryMapper;

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
        Order order = getOrder(30000, OrderStatus.INIT);
        orderMapper.save(order);
        OrderHistory orderHistory = OrderHistory.builder().order(order).currentAuditor(TEST_USER).build();

        // when
        orderHistoryMapper.save(orderHistory);

        // then
        assertThat(orderHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 아이디를 이용해 주문 히스토리 내역들을 불러온다.")
    void findByOrderId() {
        // given
        Order order = getOrder(30000, OrderStatus.INIT);
        orderMapper.save(order);
        OrderHistory orderHistory1 = getOrderHistory(order);
        OrderHistory orderHistory2 = getOrderHistory(order);
        orderHistoryMapper.save(orderHistory1);
        orderHistoryMapper.save(orderHistory2);

        // when
        List<OrderHistory> orderHistoryList = orderHistoryMapper.findByOrderId(order.getId());

        // then
        assertThat(orderHistoryList).hasSize(2)
            .extracting("price", "orderStatus", "orderId")
            .containsExactlyInAnyOrder(
                tuple(order.getPrice(), order.getOrderStatus(), order.getId()),
                tuple(order.getPrice(), order.getOrderStatus(), order.getId())
            );
    }

    private OrderHistory getOrderHistory(Order order) {
        return OrderHistory.builder()
            .order(order)
            .currentAuditor(TEST_USER)
            .build();
    }

    private Order getOrder(int price, OrderStatus orderStatus) {
        return Order.builder()
            .price(price)
            .orderStatus(orderStatus)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy(TEST_USER)
            .build();
    }
}
