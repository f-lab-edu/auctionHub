package com.flab.auctionhub.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.common.util.SessionUtil;
import com.flab.auctionhub.order.api.request.OrderCreateRequest;
import com.flab.auctionhub.order.api.request.OrderUpdateRequest;
import com.flab.auctionhub.order.application.OrderService;
import com.flab.auctionhub.order.domain.OrderStatus;
import com.flab.auctionhub.user.domain.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    MockHttpSession session;

    @BeforeEach
    void beforeEach() {
        session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.MEMBER);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() throws Exception {
        // given
        OrderCreateRequest order = getOrderRequest(30000, OrderStatus.INIT);

        // when // then
        mockMvc.perform(
            post("/orders")
                .session(session)
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("주문을 생성할때 가격은 0원을 초과하여야 합니다.")
    void createOrderCheckPrice() throws Exception {
        // given
        OrderCreateRequest order = getOrderRequest(0, OrderStatus.INIT);

        // when // then
        mockMvc.perform(
                post("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("주문 가격은 0원을 초과하여야 합니다."));
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 상태는 필수입니다.")
    void createOrderCheckOrderStatus() throws Exception {
        // given
        OrderCreateRequest order = getOrderRequest(30000, null);

        // when // then
        mockMvc.perform(
                post("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("주문 상태는 필수입니다."));
    }

    @Test
    @DisplayName("주문을 생성할 때 회원 번호는 필수입니다.")
    void createOrderCheckUserId() throws Exception {
        // given
        OrderCreateRequest order = OrderCreateRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .productId(1L)
            .userId(null)
            .build();

        // when // then
        mockMvc.perform(
                post("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("회원 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("주문을 생성할 때 상품 번호는 필수 입니다.")
    void createOrderCheckProductId() throws Exception {
        // given
        OrderCreateRequest order = OrderCreateRequest.builder()
            .price(30000)
            .orderStatus(OrderStatus.INIT)
            .productId(null)
            .userId(1L)
            .build();

        // when // then
        mockMvc.perform(
                post("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상품 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("주문 상세 내역을 불러온다.")
    void getOrder() throws Exception {
        // when // then
        mockMvc.perform(
                get("/orders/{id}", 1L)
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    @DisplayName("주문 내용을 수정한다.")
    void updateOrder() throws Exception {
        // given
        OrderUpdateRequest order = OrderUpdateRequest.builder()
            .id(1L)
            .orderStatus(OrderStatus.COMPLETED)
            .productId(1L)
            .userId(1L)
            .build();

        // when // then
        mockMvc.perform(
                put("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 내용을 수정할때 주문아이디는 필수 값이다.")
    void updateOrderCheckId() throws Exception {
        // given
        OrderUpdateRequest order = OrderUpdateRequest.builder()
            .orderStatus(OrderStatus.COMPLETED)
            .productId(1L)
            .userId(1L)
            .build();

        // when // then
        mockMvc.perform(
                put("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("주문 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("주문 내용을 수정할때 주문 상태는 필수 값이다.")
    void updateOrderCheckOrderStatus() throws Exception {
        // given
        OrderUpdateRequest order = OrderUpdateRequest.builder()
            .id(1L)
            .productId(1L)
            .userId(1L)
            .build();

        // when // then
        mockMvc.perform(
                put("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("주문 상태는 필수입니다."));
    }

    @Test
    @DisplayName("주문 내용을 수정할때 회원 번호는 필수 값이다.")
    void updateOrderCheckUserId() throws Exception {
        // given
        OrderUpdateRequest order = OrderUpdateRequest.builder()
            .id(1L)
            .orderStatus(OrderStatus.COMPLETED)
            .productId(1L)
            .build();

        // when // then
        mockMvc.perform(
                put("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("회원 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("주문 내용을 수정할때 상품 번호는 필수 값이다.")
    void updateOrderCheckProductId() throws Exception {
        // given
        OrderUpdateRequest order = OrderUpdateRequest.builder()
            .id(1L)
            .orderStatus(OrderStatus.COMPLETED)
            .userId(1L)
            .build();

        // when // then
        mockMvc.perform(
                put("/orders")
                    .session(session)
                    .content(objectMapper.writeValueAsString(order))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상품 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("유저가 주문 목록을 불러온다.")
    void getOrdersByUserId() throws Exception {
        // given
        OrderCreateRequest order = getOrderRequest(3000, OrderStatus.INIT);

        // when // then
        mockMvc.perform(
            get("/orders")
                .param("userId", String.valueOf(order.getUserId()))
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("유저 주문건에 대해 주문 상태별 목록을 불러온다.")
    void getOrderHistoryByOrderId() throws Exception {
        // when // then
        mockMvc.perform(
                get("/orders/history/{id}", 1L)
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    private OrderCreateRequest getOrderRequest(int price, OrderStatus status) {
        return OrderCreateRequest.builder()
            .price(price)
            .orderStatus(status)
            .productId(1L)
            .userId(1L)
            .build();
    }
}
