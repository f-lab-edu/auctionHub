package com.flab.auctionhub.bid.api;

import com.flab.auctionhub.bid.api.request.BidCreateRequest;
import com.flab.auctionhub.bid.application.BidService;
import com.flab.auctionhub.bid.application.response.BidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    /**
     * 입찰을 등록한다.
     * @param request 입찰에 필요한 정보
     */
    @PostMapping("/bids")
    public ResponseEntity<Long> createBid(@RequestBody @Validated BidCreateRequest request) {
        Long id = bidService.createBid(request.toServiceRequest());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * 상품에 따른 입찰 내역을 불러온다.
     * @param productId 상품 번호
     */
    @GetMapping("/bids/{productId}")
    public ResponseEntity<List<BidResponse>> getBidListForProduct(@PathVariable Long productId) {
        List<BidResponse> bidList = bidService.getBidListForProduct(productId);
        return new ResponseEntity<>(bidList, HttpStatus.OK);
    }
}
