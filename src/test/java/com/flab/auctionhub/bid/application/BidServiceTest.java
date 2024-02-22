package com.flab.auctionhub.bid.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

import com.flab.auctionhub.bid.application.request.BidCreateServiceRequest;
import com.flab.auctionhub.bid.application.response.BidResponse;
import com.flab.auctionhub.bid.exception.InvalidPriceException;
import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.common.audit.LoginUserAuditorAware;
import com.flab.auctionhub.product.dao.ProductMapper;
import com.flab.auctionhub.product.domain.Product;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BidServiceTest {

    @Autowired
    private BidService bidService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @MockBean
    private LoginUserAuditorAware loginUserAuditorAware;

    User user;

    Product product;

    Category category;

    @BeforeEach
    void BeforeEach() {
        user = User.builder()
            .userId("userId")
            .password("testpassword")
            .username("username")
            .phoneNumber("010-0000-0000")
            .build();
        userMapper.save(user);

        category = Category.builder()
            .name(CategoryType.MENSCLOTHING)
            .createdBy("admin")
            .build();
        categoryMapper.save(category);

        product = Product.builder()
            .name("나이키 슈즈")
            .description("사이즈는 280입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(1000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2023,12,22,05,44,37))
            .endedAt(LocalDateTime.of(2023,12,25,05,44,37))
            .createdBy("seller")
            .userId(user.getId())
            .categoryId(category.getId())
            .build();
        productMapper.save(product);
    }

    @Test
    @DisplayName("입찰을 등록한다")
    void createBid() {
        // given
        BidCreateServiceRequest request = BidCreateServiceRequest.builder()
            .price(2000)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("testUser"));

        // when
        Long id = bidService.createBid(request);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("입찰을 등록할 때 입찰금액이 100원 단위가 아니면 예외가 발생한다.")
    void checkBidPriceFormat() {
        // given
        BidCreateServiceRequest request = BidCreateServiceRequest.builder()
            .price(999)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("testUser"));

        // when // then
        assertThatThrownBy(() -> bidService.createBid(request))
            .isInstanceOf(InvalidPriceException.class)
            .hasMessage("입찰 가격은 100원 단위 이어야 합니다.");
    }

    @Test
    @DisplayName("입찰을 등록할 때 입찰금액이 상품의 최고 입찰액보다 높지 않으면 예외가 발생한다.")
    void checkBidPriceFormat2() {
        // given
        BidCreateServiceRequest request = BidCreateServiceRequest.builder()
            .price(900)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("testUser"));

        // when // then
        assertThatThrownBy(() -> bidService.createBid(request))
            .isInstanceOf(InvalidPriceException.class)
            .hasMessage("상품의 최고 입찰액보다 입찰금액이 높아야 합니다.");
    }

    @Test
    @DisplayName("상품에 따른 입찰 내역을 불러온다.")
    void getBidListForProduct() {
        // given
        BidCreateServiceRequest request1 = BidCreateServiceRequest.builder()
            .price(2000)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        when(loginUserAuditorAware.getCurrentAuditor()).thenReturn(Optional.of("testUser"));
        bidService.createBid(request1);
        BidCreateServiceRequest request2 = BidCreateServiceRequest.builder()
            .price(3000)
            .userId(user.getId())
            .productId(product.getId())
            .build();
        bidService.createBid(request2);

        // when
        List<BidResponse> bidResponseList = bidService.getBidListForProduct(product.getId());

        // then
        assertThat(bidResponseList).hasSize(2)
            .extracting("price","userId","productId")
            .containsExactlyInAnyOrder(
                tuple(request1.getPrice(), request1.getUserId(), request1.getProductId()),
                tuple(request2.getPrice(), request2.getUserId(), request2.getProductId())
            );
    }
}
