package com.flab.auctionhub.product.application;

import com.flab.auctionhub.product.dao.ProductMapper;
import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.product.dto.request.ProductCreateRequest;
import com.flab.auctionhub.product.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public Long createProduct(ProductCreateRequest request) {
        Product product = request.toEntity();
        productMapper.save(product);
        System.out.println(product.getId());
        return product.getId();
    }

    public List<ProductResponse> getSellingProducts() {

        List<Product> productList = productMapper.findAllBySellingStatusIn(
            ProductSellingStatus.forDisplay());

        return productList.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
