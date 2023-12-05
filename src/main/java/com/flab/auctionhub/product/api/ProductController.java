package com.flab.auctionhub.product.api;

import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.product.dto.request.ProductCreateRequest;
import com.flab.auctionhub.product.dto.response.ProductResponse;
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
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Long> createProduct(@RequestBody ProductCreateRequest request) {
        Long id = productService.createProduct(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

}
