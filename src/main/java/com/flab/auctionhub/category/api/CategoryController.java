package com.flab.auctionhub.category.api;

import com.flab.auctionhub.category.application.CategoryService;
import com.flab.auctionhub.category.dto.resoponse.CategoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> findAllCategory() {
        List<CategoryResponse> categoryList = categoryService.findAllCategory();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

}
