package com.flab.auctionhub.product.application;

import com.flab.auctionhub.category.application.CategoryService;
import com.flab.auctionhub.product.application.request.ProductCreateServiceRequest;
import com.flab.auctionhub.product.application.request.ProductUpdateServiceRequest;
import com.flab.auctionhub.product.application.response.ProductResponse;
import com.flab.auctionhub.product.dao.ProductMapper;
import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.product.exception.ProductNotFoundException;
import com.flab.auctionhub.user.application.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    private final UserService userService;

    private final CategoryService categoryService;

    /**
     * 상품을 등록한다
     * @param request 상품 등록에 필요한 정보
     */
    @Transactional
    public Long createProduct(ProductCreateServiceRequest request) {
        // 회원 여부 조회
        userService.findById(request.getUserId());
        // 카테고리 존재 여부 조회
        categoryService.findById(request.getCategoryId());
        Product product = request.toEntity();
        productMapper.save(product);
        return product.getId();
    }

    /**
     * 상품 여러개를 등록한다
     * @param request 상품 등록에 필요한 정보
     */
    @Transactional
    public List<Long> createProducts(List<ProductCreateServiceRequest> request) {
        List<Product> productList = new ArrayList<>();

        for (ProductCreateServiceRequest productCreateServiceRequest : request) {
            // 회원 여부 조회
            userService.findById(productCreateServiceRequest.getUserId());
            // 카테고리 존재 여부 조회
            categoryService.findById(productCreateServiceRequest.getCategoryId());
            Product product = productCreateServiceRequest.toEntity();
            productList.add(product);
        }
        productMapper.saveAll(productList);

        return productList.stream()
            .map(Product::getId)
            .collect(Collectors.toList());
    }

    /**
     * 판매중, 판매대기인 상품을 조회한다.
     */
    public List<ProductResponse> getSellingProducts() {

        List<Product> productList = productMapper.findAllBySellingStatusIn(
            ProductSellingStatus.forDisplay());

        return productList.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * 상품 id로 상품을 조회한다.
     * @param id 상품 아이디
     */
    public ProductResponse findById(Long id) {
        return productMapper.findById(id)
            .map(ProductResponse::of)
            .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    /**
     * 상품을 수정한다.
     * @param request 상품 수정에 필요한 정보
     */
    public ProductResponse update(ProductUpdateServiceRequest request) {
        // 회원 여부 주회
        userService.findById(request.getUserId());
        // 카테 고리 존재 여부 조회
        categoryService.findById(request.getCategoryId());

        Product product = request.toEntity();
        productMapper.update(product);
        return productMapper.findById(request.getId())
            .map(ProductResponse::of)
            .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    /**
     * 전체 상품을 조회한다.
     */
    public List<ProductResponse> findAllProduct() {
        return productMapper.findAll().stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
