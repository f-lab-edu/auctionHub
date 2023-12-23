package com.flab.auctionhub.product.application;

import com.flab.auctionhub.product.application.request.ProductCreateServiceRequest;
import com.flab.auctionhub.product.application.response.ProductResponse;
import com.flab.auctionhub.product.dao.ProductMapper;
import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.application.UserService;
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


    @Transactional
    public Long createProduct(ProductCreateServiceRequest request) {

        userService.findById(request.getUserId());

        Product product = request.toEntity();
        productMapper.save(product);
        return product.getId();
    }

    public List<ProductResponse> getSellingProducts() {

        List<Product> productList = productMapper.findAllBySellingStatusIn(
            ProductSellingStatus.forDisplay());

        return productList.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findAllProduct() {
        return productMapper.findAll().stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
