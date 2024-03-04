package com.flab.auctionhub.bid.api;

import com.flab.auctionhub.bid.api.request.BidCreateRequest;
import com.flab.auctionhub.bid.application.BidService;
import com.flab.auctionhub.bid.application.response.BidResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * 상품에 따른 입찰 내역을 불러온다.
     * @param productId 상품 번호
     */
    @GetMapping("/bids")
    public ResponseEntity<List<BidResponse>> getBidsByProductId(@RequestParam Long productId) {
        List<BidResponse> bidList = bidService.findBidsByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(bidList);
    }
}
