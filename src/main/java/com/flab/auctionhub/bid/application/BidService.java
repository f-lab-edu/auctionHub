package com.flab.auctionhub.bid.application;

import com.flab.auctionhub.bid.application.request.BidCreateServiceRequest;
import com.flab.auctionhub.bid.application.response.BidResponse;
import com.flab.auctionhub.bid.dao.BidMapper;
import com.flab.auctionhub.bid.domain.Bid;
import com.flab.auctionhub.bid.exception.InvalidPriceException;
import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.product.application.response.ProductResponse;
import com.flab.auctionhub.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BidService {

    private final BidMapper bidMapper;

    private final UserService userService;

    private final ProductService productService;

    /**
     * 입찰을 등록한다.
     * @param request 입찰 등록에 필요한 정보
     */
    @Transactional
    public Long createBid(BidCreateServiceRequest request) {

        // 회원 여부 조회
        userService.findById(request.getUserId());

        // 상품 존재 여부 조회
        ProductResponse productResponse = productService.findById(request.getProductId());

        checkValidPrice(request, productResponse);

        Bid bid = request.toEntity();
        bidMapper.save(bid);
        return bid.getId();
    }

    /**
     * 상품에 따른 입찰 내역을 불러온다.
     * @param productId 상품 번호
     */
    public List<BidResponse> getBidListForProduct(Long productId) {
        List<Bid> bidList = bidMapper.findByProductId(productId);

        return bidList.stream()
            .map(BidResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * 입찰 금액이 조건에 맞는지 체크한다.
     */
    private void checkValidPrice(BidCreateServiceRequest request, ProductResponse productResponse) {
        int price = request.getPrice();
        if (isValidPrice(price)) {
            throw new InvalidPriceException("입찰 가격은 100원 단위 이어야 합니다.");
        }

        Integer highestBid = bidMapper.selectHighestBidPriceForProduct(
            request.getProductId()).orElseGet(() -> productResponse.getMinBidPrice());
        if (price < highestBid) {
            throw new InvalidPriceException("상품의 최고 입찰액보다 입찰금액이 높아야 합니다.");
        }
    }

    /**
     * 입찰 금액이 100원 단위인지 체크한다.
     */
    private boolean isValidPrice(int price) {
        return price % 100 != 0;
    }

}