package com.flab.auctionhub.product.dao;

import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    Product save(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

}
