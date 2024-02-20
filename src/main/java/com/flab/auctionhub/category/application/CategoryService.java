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

    /**
     * 카테고리를 등록한다.
     * @param request 카테고리 등록에 필요한 정보
     */
    @Transactional
    public Long createCategory(CategoryServiceRequest request) {
        Category category = request.toEntity();
        categoryMapper.save(category);
        return category.getId();
    }

    /**
     * 카테고리 전체를 조회한다.
     */
    public List<CategoryResponse> findAllCategory() {
        return categoryMapper.findAll().stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * 카테고리 번호로 상세 조회한다.
     * @param id 카테고리 아이디
     */
    public CategoryResponse findById(Long id) {
        return categoryMapper.findById(id)
            .map(CategoryResponse::of)
            .orElseThrow(() -> new CategoryNotFoundException("해당 카테고리를 찾을 수 없습니다."));

    }
}
