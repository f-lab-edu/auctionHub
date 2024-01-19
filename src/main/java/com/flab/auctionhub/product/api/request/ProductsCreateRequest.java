package com.flab.auctionhub.product.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsCreateRequest {

    @Size(min = 1, max = 3, message = "최소 {min}개 이상 최대 {max}개 이하여야 합니다.")
    @Valid
    List<ProductCreateRequest> productCreateRequestList;
}
