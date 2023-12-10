package com.flab.auctionhub.category.api;

import com.flab.auctionhub.category.application.CategoryService;
import com.flab.auctionhub.category.dto.request.CategoryRequest;
import com.flab.auctionhub.category.dto.resoponse.CategoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Long> createCategory(@RequestBody CategoryRequest request) {
        Long id = categoryService.createCategory(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> findAllCategory() {
        List<CategoryResponse> categoryList = categoryService.findAllCategory();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

}
