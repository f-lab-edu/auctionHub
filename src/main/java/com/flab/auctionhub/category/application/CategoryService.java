package com.flab.auctionhub.category.application;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.dto.resoponse.CategoryResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> findAllCategory() {
        return categoryMapper.findAll().stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

}
