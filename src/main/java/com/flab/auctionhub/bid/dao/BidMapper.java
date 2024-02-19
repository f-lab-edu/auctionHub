package com.flab.auctionhub.bid.dao;

import com.flab.auctionhub.bid.domain.Bid;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Optional;

@Mapper
public interface BidMapper {

    void save(Bid bid);

    Optional<Integer> selectHighestBidPriceForProduct(Long id);

    List<Bid> findByProductId(Long id);
}
