package com.flab.auctionhub.product.dao;

import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    void save(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Product> update(Product product);

    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

}
