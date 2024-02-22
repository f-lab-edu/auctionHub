package com.flab.auctionhub.bid.dao;

import com.flab.auctionhub.bid.domain.Bid;
import com.flab.auctionhub.category.dao.CategoryMapper;
import com.flab.auctionhub.category.domain.Category;
import com.flab.auctionhub.category.domain.CategoryType;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BidMapperTest {

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

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
    @DisplayName("입찰내용을 저장한다.")
    void save() {
        // given
        Bid bid = getBid(1000);

        // when
        bidMapper.save(bid);

        // then
        assertThat(bid.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품 번호로 최대 입찰 금액을 가져온다.")
    void selectHighestBidPriceForProduct() {
        // given
        Bid bid1 = getBid(1000);
        Bid bid2 = getBid(2000);
        Bid bid3 = getBid(3000);
        bidMapper.save(bid1);
        bidMapper.save(bid2);
        bidMapper.save(bid3);

        // when
        Integer result = bidMapper.selectHighestBidPriceForProduct(product.getId()).get();

        // then
        assertThat(result).isEqualTo(3000);
    }

    @Test
    @DisplayName("상품번호로 해당 상품의 입찰 내역을 불러온다.")
    void findByProductId() {
        // given
        Bid bid1 = getBid(1000);
        Bid bid2 = getBid(2000);
        Bid bid3 = getBid(3000);
        bidMapper.save(bid1);
        bidMapper.save(bid2);
        bidMapper.save(bid3);

        // when
        List<Bid> bidList = bidMapper.findByProductId(product.getId());

        // then
        assertThat(bidList).hasSize(3)
            .extracting("price", "userId","productId")
            .containsExactlyInAnyOrder(
                tuple(bid1.getPrice(), bid1.getUserId(), bid1.getProductId()),
                tuple(bid2.getPrice(), bid2.getUserId(), bid2.getProductId()),
                tuple(bid3.getPrice(), bid3.getUserId(), bid3.getProductId())
            );
    }

    private Bid getBid(int price) {
        return Bid.builder()
            .price(price)
            .userId(user.getId())
            .productId(product.getId())
            .createdBy("testUser")
            .build();
    }
}
