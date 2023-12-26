package com.flab.auctionhub.product.api;

import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.product.api.request.ProductCreateRequest;
import com.flab.auctionhub.product.application.response.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Long> createProduct(@RequestBody @Validated ProductCreateRequest request) {
        Long id = productService.createProduct(request.toServiceRequest());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> findAllProduct() {
        List<ProductResponse> productList = productService.findAllProduct();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products/selling")
    public ResponseEntity<List<ProductResponse>> getSellingProducts() {
        List<ProductResponse> productList = productService.getSellingProducts();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

}
