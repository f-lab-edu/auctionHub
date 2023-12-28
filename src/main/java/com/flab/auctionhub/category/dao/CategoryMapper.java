package com.flab.auctionhub.category.dao;

import com.flab.auctionhub.category.domain.Category;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    void save(Category category);

    List<Category> findAll();

    Optional<Category> findById(Long id);
}
