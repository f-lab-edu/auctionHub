package com.flab.auctionhub.product.api;

import com.flab.auctionhub.product.api.request.ProductCreateRequest;
import com.flab.auctionhub.product.api.request.ProductUpdateRequest;
import com.flab.auctionhub.product.api.request.ProductsCreateRequest;
import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.product.application.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품을 등록한다.
     * @param request 상품 등록에 필요한 정보
     */
    @PostMapping("/products")
    public ResponseEntity<List<Long>> createProducts(@RequestBody @Validated ProductsCreateRequest request) {
        List<Long> productListId = productService.createProducts(
            request.getProductCreateRequestList().stream()
                .map(ProductCreateRequest::toServiceRequest)
                .collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(productListId);
    }

    /**
     * 전체 상품을 조회한다.
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productList = productService.findAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    /**
     * 상품 id로 상품을 조회한다.
     * @param id 상품 아이디
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.findProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    /**
     * 상품 수정을 한다.
     * @param request 상품 수정에 필요한정보
     */
    @PutMapping("/products")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody @Validated ProductUpdateRequest request) {
        ProductResponse product = productService.updateProduct(request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    /**
     * 판매중, 판매대기인 상품을 조회한다.
     */
    @GetMapping("/products/selling")
    public ResponseEntity<List<ProductResponse>> getSellingProducts() {
        List<ProductResponse> productList = productService.getSellingProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

}
