package com.flab.auctionhub.category.application;

import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.dto.request.CategoryRequest;
import com.flab.auctionhub.category.dto.resoponse.CategoryResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    @Transactional
    public Long createCategory(CategoryRequest request) {
        Category category = request.toEntity();
        categoryMapper.save(category);
        return category.getId();
    }

    public List<CategoryResponse> findAllCategory() {
        return categoryMapper.findAll().stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

}
