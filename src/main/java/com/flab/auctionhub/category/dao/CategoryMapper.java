package com.flab.auctionhub.category.dao;

import com.flab.auctionhub.category.domain.Category;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    void save(Category category);

    List<Category> findAll();

}
