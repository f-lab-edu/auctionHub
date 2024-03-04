package com.flab.auctionhub.order.application;

import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import com.flab.auctionhub.order.application.request.OrderCreateServiceRequest;
import com.flab.auctionhub.order.application.request.OrderUpdateServiceRequest;
import com.flab.auctionhub.order.application.response.OrderResponse;
import com.flab.auctionhub.order.dao.OrderHistoryMapper;
import com.flab.auctionhub.order.dao.OrderMapper;
import com.flab.auctionhub.order.domain.Order;
import com.flab.auctionhub.order.domain.OrderHistory;
import com.flab.auctionhub.order.exception.OrderNotFoundException;
import com.flab.auctionhub.order.exception.OrderNotPossibleException;
import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.user.application.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderHistoryMapper orderHistoryMapper;
    private final UserService userService;
    private final ProductService productService;
    private final LoginUserAuditorAware loginUserAuditorAware;

    /**
     * 주문을 생성한다
     * @param request 주문 생성에 필요한 정보
     */
    @Transactional
    public Long createOrder(OrderCreateServiceRequest request) {
        // 회원 여부 조회
        userService.findUserById(request.getUserId());
        // 주문 가능한 상품인지 체크
        if (!productService.checkProductOrderAvailability(request.getProductId(), request.getPrice())) {
            throw new OrderNotPossibleException("주문 가능한 상품이 아닙니다.");
        }
        String currentAuditor = loginUserAuditorAware.getCurrentAuditor().get();
        Order order = request.toEntity(currentAuditor);
        orderMapper.save(order);
        OrderHistory orderHistory = getOrderHistory(order, currentAuditor);
        orderHistoryMapper.save(orderHistory);
        return order.getId();
    }

    /**
     * 주문 상세 내역을 불러온다.
     * @param id 주문 아이디
     */
    public OrderResponse getOrder(Long id) {
        return orderMapper.findById(id)
            .map(OrderResponse::of)
            .orElseThrow(() -> new OrderNotFoundException("해당 주문 건을 찾을 수 없습니다."));
    }

    /**
     * 주문 상세 정보를 수정한다.
     * @param request 수정에 필요한 정보
     */
    @Transactional
    public OrderResponse updateOrder(OrderUpdateServiceRequest request) {
        // 회원 여부 조회
        userService.findUserById(request.getUserId());

        // 상품 존재 여부 조회
        productService.findProductById(request.getProductId());

        String currentAuditor = loginUserAuditorAware.getCurrentAuditor().get();
        Order order = request.toEntity(currentAuditor);
        orderMapper.update(order);

        OrderHistory orderHistory = getOrderHistory(order, currentAuditor);
        orderHistoryMapper.save(orderHistory);
        return getOrder(order.getId());
    }

    /**
     * 유저가 주문한 목록을 불러온다.
     * @param userId 유저 아이디
     */
    public List<OrderResponse> findOrdersByUserId(Long userId) {
        List<Order> orderList = orderMapper.findAllByUserId(userId);
        return orderList.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * 유저 주문건에 대해 주문 상태별 목록을 불러온다.
     * @param orderId 주문 아이디
     */
    public List<OrderResponse> findOrderHistoryByOrderId(Long orderId) {
        List<OrderHistory> orderHistoryList = orderHistoryMapper.findByOrderId(orderId);
        return orderHistoryList.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * 주문 히스토리 객체를 불러온다.
     * @param order 주문 정보
     * @param currentAuditor 사용
     */
    private OrderHistory getOrderHistory(Order order, String currentAuditor) {
        return OrderHistory.builder()
            .order(order)
            .currentAuditor(currentAuditor)
            .build();
    }
}
