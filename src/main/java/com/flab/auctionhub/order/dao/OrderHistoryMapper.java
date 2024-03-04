package com.flab.auctionhub.order.dao;

import com.flab.auctionhub.order.domain.OrderHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderHistoryMapper {

    void save(OrderHistory order);

    List<OrderHistory> findByOrderId(Long id);
}
