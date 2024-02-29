package com.flab.auctionhub.order.application;

import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import com.flab.auctionhub.order.application.request.OrderCreateServiceRequest;
import com.flab.auctionhub.order.application.request.OrderUpdateServiceRequest;
import com.flab.auctionhub.order.application.response.OrderResponse;
import com.flab.auctionhub.order.dao.OrderMapper;
import com.flab.auctionhub.order.domain.Order;
import com.flab.auctionhub.order.exception.OrderNotFoundException;
import com.flab.auctionhub.order.exception.OrderNotPossibleException;
import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
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
        userService.findById(request.getUserId());
        // 주문 가능한 상품인지 체크
        if (!productService.checkProductOrderAvailability(request.getProductId(), request.getPrice())) {
            throw new OrderNotPossibleException("주문 가능한 상품이 아닙니다.");
        }
        String currentAuditor = loginUserAuditorAware.getCurrentAuditor().get();
        Order order = request.toEntity(currentAuditor);
        orderMapper.save(order);
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
    public OrderResponse updateOrder(OrderUpdateServiceRequest request) {
        // 회원 여부 조회
        userService.findById(request.getUserId());
        // 상품 존재 여부 조회
        productService.findById(request.getProductId());
        String currentAuditor = loginUserAuditorAware.getCurrentAuditor().get();
        Order order = request.toEntity(currentAuditor);
        orderMapper.update(order);
        return getOrder(order.getId());
    }

    /**
     * 유저가 주문한 목록을 불러온다.
     * @param userId 유저 아이디
     */
    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orderList = orderMapper.findAllByUserId(userId);
        return orderList.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }
}
