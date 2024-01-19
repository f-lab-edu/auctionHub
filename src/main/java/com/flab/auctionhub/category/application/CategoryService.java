package com.flab.auctionhub.category.application;

import com.flab.auctionhub.category.application.request.CategoryServiceRequest;
import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.application.response.CategoryResponse;
import java.util.List;
import java.util.stream.Collectors;
import com.flab.auctionhub.category.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    @Transactional
    public Long createCategory(CategoryServiceRequest request) {
        Category category = request.toEntity();
        categoryMapper.save(category);
        return category.getId();
    }

    public List<CategoryResponse> findAllCategory() {
        return categoryMapper.findAll().stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    public CategoryResponse findById(Long id) {
        return categoryMapper.findById(id)
            .map(CategoryResponse::of)
            .orElseThrow(() -> new CategoryNotFoundException("해당 카테고리를 찾을 수 없습니다."));

    }
}
