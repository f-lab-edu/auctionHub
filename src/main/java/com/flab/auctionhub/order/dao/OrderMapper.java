package com.flab.auctionhub.order.dao;

import com.flab.auctionhub.order.domain.Order;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void save(Order order);

    Optional<Order> findById(Long id);

    void update(Order order);

    List<Order> findAllByUserId(Long userId);
}
