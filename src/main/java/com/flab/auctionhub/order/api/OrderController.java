package com.flab.auctionhub.order.api;

import com.flab.auctionhub.order.api.request.OrderCreateRequest;
import com.flab.auctionhub.order.api.request.OrderUpdateRequest;
import com.flab.auctionhub.order.application.OrderService;
import com.flab.auctionhub.order.application.response.OrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문을 생성한다.
     * @param request 주문 생성에 필요한 정보
     */
    @PostMapping("/orders")
    public ResponseEntity<Long> createOrder(@RequestBody @Validated OrderCreateRequest request) {
        Long id = orderService.createOrder(request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * 주문 상세 내역을 불러온다.
     * @param id 주문 아이디
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        OrderResponse order = orderService.getOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    /**
     * 주문 내용을 수정한다.
     * @param request 주문 수정에 필요한 정보
     */
    @PutMapping("/orders")
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody @Validated OrderUpdateRequest request) {
        OrderResponse order = orderService.updateOrder(request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    /**
     * 유저가 주문한 목록을 불러온다.
     * @param userId 유저 아이디
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@RequestParam Long userId) {
        List<OrderResponse> orderList = orderService.findOrdersByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    /**
     * 유저 주문건에 대해 주문 상태별 목록을 불러온다.
     * @param id 주문 아이디
     */
    @GetMapping("/orders/history/{id}")
    public ResponseEntity<List<OrderResponse>> getOrderHistoryByOrderId(@PathVariable Long id) {
        List<OrderResponse> orderHistoryList = orderService.findOrderHistoryByOrderId(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderHistoryList);
    }
}
