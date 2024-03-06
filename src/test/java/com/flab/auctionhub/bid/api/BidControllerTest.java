package com.flab.auctionhub.bid.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.bid.api.request.BidCreateRequest;
import com.flab.auctionhub.bid.application.BidService;
import com.flab.auctionhub.common.util.SessionUtil;
import com.flab.auctionhub.user.domain.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BidController.class)
class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidService bidService;

    @Autowired
    private ObjectMapper objectMapper;

    MockHttpSession session;

    @BeforeEach
    void beforeEach() {
        session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.MEMBER);
    }

    @Test
    @DisplayName("신규 입찰을 등록한다.")
    void createBid() throws Exception {
        // given
        BidCreateRequest bid = getBidRequest(3000);

        // when // then
        mockMvc.perform(
            post("/bids")
                .session(session)
                .content(objectMapper.writeValueAsString(bid))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("신규 입찰가격은 0원을 초과하여야 합니다.")
    void createBidCheckPrice() throws Exception {
        // given
        BidCreateRequest bid = getBidRequest(0);

        // when // then
        mockMvc.perform(
                post("/bids")
                    .session(session)
                    .content(objectMapper.writeValueAsString(bid))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("입찰 가격은 0원을 초과하여야 합니다."));
    }

    @Test
    @DisplayName("신규 입찰을 등록할 때 회원 번호는 필수 입니다.")
    void createBidCheckUserId() throws Exception {
        // given
        BidCreateRequest bid = BidCreateRequest.builder()
            .price(1000)
            .productId(1L)
            .build();

        // when // then
        mockMvc.perform(
                post("/bids")
                    .session(session)
                    .content(objectMapper.writeValueAsString(bid))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("회원 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("신규 입찰을 등록할 때 상품 번호는 필수 입니다.")
    void createBidCheckProductId() throws Exception {
        // given
        BidCreateRequest bid = BidCreateRequest.builder()
            .price(1000)
            .userId(1L)
            .build();

        // when // then
        mockMvc.perform(
                post("/bids")
                    .session(session)
                    .content(objectMapper.writeValueAsString(bid))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상품 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("상품에 따른 입찰 내역을 불러온다.")
    void getBidsByProductId() throws Exception {
        // given
        BidCreateRequest bid = getBidRequest(2000);

        // when // then
        mockMvc.perform(
                get("/bids", bid.getProductId())
                    .param("productId", String.valueOf(bid.getProductId()))
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("상품에 따른 최고 입찰액을 불러온다.")
    void getHighestPrice() throws Exception {
        // given
        BidCreateRequest bid = getBidRequest(2000);

        // when // then
        mockMvc.perform(
                get("/bids/highest", bid.getProductId())
                    .param("productId", String.valueOf(bid.getProductId()))
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    private BidCreateRequest getBidRequest(int price) {
        return BidCreateRequest.builder()
            .price(price)
            .userId(1L)
            .productId(1L)
            .build();
    }
}
