package com.flab.auctionhub.bid.dao;

import com.flab.auctionhub.bid.domain.Bid;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BidMapper {

    void save(Bid bid);

    Optional<Integer> selectHighestBidPriceForProduct(Long id);

    List<Bid> findAllByProductId(Long id);
}
