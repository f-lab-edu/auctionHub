package com.flab.auctionhub.bid.application;

import com.flab.auctionhub.bid.application.request.BidCreateServiceRequest;
import com.flab.auctionhub.bid.application.response.BidResponse;
import com.flab.auctionhub.bid.dao.BidMapper;
import com.flab.auctionhub.bid.domain.Bid;
import com.flab.auctionhub.bid.exception.InvalidPriceException;
import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.product.application.response.ProductResponse;
import com.flab.auctionhub.user.application.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BidService {

    private final BidMapper bidMapper;
    private final UserService userService;
    private final ProductService productService;
    private final LoginUserAuditorAware loginUserAuditorAware;
    private final RedisTemplate<String, String> redisTemplate;
    private final String HIGHEST_BID_KEY = "highestBid";

    /**
     * 입찰을 등록한다.
     * @param request 입찰 등록에 필요한 정보
     */
    @Transactional
    public Long createBid(BidCreateServiceRequest request) {
        // 회원 여부 조회
        userService.findUserById(request.getUserId());

        // 상품 존재 여부 조회
        ProductResponse productResponse = productService.findProductById(request.getProductId());

        String currentAuditor = loginUserAuditorAware.getCurrentAuditor().get();
        checkValidPrice(request, productResponse);

        // Redis를 사용하여 최고 입찰액 업데이트
        updateHighestBidInCache(request.getProductId(), request.getPrice());

        Bid bid = request.toEntity(currentAuditor);
        bidMapper.save(bid);
        return bid.getId();
    }

    /**
     * 상품에 따른 입찰 내역을 불러온다.
     * @param productId 상품 번호
     */
    public List<BidResponse> findBidsByProductId(Long productId) {
        List<Bid> bidList = bidMapper.findAllByProductId(productId);
        return bidList.stream()
            .map(BidResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * 상품에 따른 최고 입찰액을 불러온다.
     * @param productId 상품 번호
     */
    public Integer findHighestPrice(Long productId) {
        String key = HIGHEST_BID_KEY + productId;
        String highestBid = redisTemplate.opsForValue().get(key);

        if (highestBid == null) {
            highestBid = bidMapper.selectHighestBidPriceForProduct(productId)
                .orElseGet(() -> productService.findProductById(productId).getMinBidPrice())
                .toString();
            redisTemplate.opsForValue().set(key, highestBid);
        }
        return Integer.valueOf(highestBid);
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
        if (price <= highestBid) {
            throw new InvalidPriceException("상품의 최고 입찰액보다 입찰금액이 높아야 합니다.");
        }
    }

    /**
     * 입찰 금액이 100원 단위인지 체크한다.
     */
    private boolean isValidPrice(int price) {
        return price % 100 != 0;
    }

    /**
     * Redis에 저장된 최고 입찰액을 업데이트한다.
     * @param productId 상품 아이디
     * @param newBidPrice 새로운 입찰 금액
     */
    private void updateHighestBidInCache(Long productId, int newBidPrice) {
        String key = HIGHEST_BID_KEY + productId;
        String currentHighestBid = redisTemplate.opsForValue().get(key);

        int currentPrice = (currentHighestBid != null) ? Integer.parseInt(currentHighestBid) : 0;

        if (newBidPrice > currentPrice) {
            redisTemplate.opsForValue().set(key, String.valueOf(newBidPrice));
        }
    }
}
