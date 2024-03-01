package com.flab.auctionhub.product.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.common.util.SessionUtil;
import com.flab.auctionhub.product.api.request.ProductCreateRequest;
import com.flab.auctionhub.product.api.request.ProductUpdateRequest;
import com.flab.auctionhub.product.api.request.ProductsCreateRequest;
import com.flab.auctionhub.product.application.ProductService;
import com.flab.auctionhub.product.application.response.ProductResponse;
import com.flab.auctionhub.product.domain.ProductSellingStatus;
import com.flab.auctionhub.user.domain.UserRoleType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    MockHttpSession session;

    @BeforeEach
    void beforeEach() {
        session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.SELLER);
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void createProducts() throws Exception {
        // given
        ProductCreateRequest request = getProductRequest("나이키 슈즈", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
            post("/products")
                .session(session)
                .content(objectMapper.writeValueAsString(productsCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("상품을 등록할 때 최소 1개 이상 최대 3개 이하여야 합니다.")
    void createProductsCheckSize() throws Exception {
        // given
        ProductCreateRequest request1 = getProductRequest("나이키 슈즈1", "정품이고 250입니다.", ProductSellingStatus.SELLING);
        ProductCreateRequest request2 = getProductRequest("나이키 슈즈2", "정품이고 260입니다.", ProductSellingStatus.SELLING);
        ProductCreateRequest request3 = getProductRequest("나이키 슈즈3", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        ProductCreateRequest request4 = getProductRequest("나이키 슈즈4", "정품이고 280입니다.", ProductSellingStatus.SELLING);
        List<ProductCreateRequest> productCreateRequestList = Arrays.asList(request1, request2, request3, request4);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("최소 1개 이상 최대 3개 이하여야 합니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 상품 이름은 필수 값이다.")
    void createProductsWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name(null)
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 상품 상태는 필수 값이다.")
    void createProductsWithoutProductSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상품 상태는 필수입니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 즉시 구매가는 0원을 초과하여야 한다.")
    void createProductsCheckQuickPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(0)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("즉시 구매가는 0원을 초과하여야 합니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 즉시 구매가는 0원을 초과하여야 한다.")
    void createProductsCheckStartBidPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(0)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("시작 입찰액은 0원을 초과하여야 합니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 시작 입찰액은 0원을 초과하여야 한다.")
    void createProductsCheckMinBidPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(0)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("최소 입찰액은 0원을 초과하여야 합니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 경매 시작시간은 필수다.")
    void createProductsCheckStartedAt() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .userId(1L)
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("경매 시작시간은 필수입니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 회원번호는 필수다.")
    void createProductsWithoutUserId() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .categoryId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("회원 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("상품을 등록할 때 카테고리번호는 필수다.")
    void createProductsWithoutCategoryId() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .build();
        List<ProductCreateRequest> productCreateRequestList = Collections.singletonList(request);
        ProductsCreateRequest productsCreateRequest = new ProductsCreateRequest(productCreateRequestList);

        // when // then
        mockMvc.perform(
                post("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(productsCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 번호는 필수 입니다."));
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void getAllProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.findAllProducts()).thenReturn(result);

        // when // then
        mockMvc.perform(
            get("/products")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("상품번호를 이용해 해당 상품을 조회한다.")
    void getProductById() throws Exception {
        // given
        ProductCreateRequest request = getProductRequest("나이키 슈즈", "정품이고 270입니다.", ProductSellingStatus.SELLING);
        productService.createProducts(List.of(request.toServiceRequest()));
        Long id = 1L;
        ProductResponse productResponse = ProductResponse.builder()
            .id(id)
            .name(request.getName())
            .description(request.getDescription())
            .sellingStatus(request.getSellingStatus())
            .quickPrice(request.getQuickPrice())
            .startBidPrice(request.getStartBidPrice())
            .minBidPrice(request.getMinBidPrice())
            .currentBidPrice(request.getMinBidPrice())
            .startedAt(request.getStartedAt())
            .build();
        when(productService.findProductById(id)).thenReturn(productResponse);

        // when // then
        mockMvc.perform(
            get("/products/{id}", id)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("상품을 수정한다.")
    void updateProduct() throws Exception {
        // given
        ProductUpdateRequest request = ProductUpdateRequest.builder()
            .id(1L)
            .name("나이키 슈즈")
            .description("정품이고 270입니다.")
            .sellingStatus(ProductSellingStatus.SELLING)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .build();

        MockHttpSession session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.SELLER);

        // when // then
        mockMvc.perform(
                put("/products")
                    .session(session)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("입찰 가능한 상품을 조회한다.")
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        // when // then
        mockMvc.perform(
            get("/products/selling")
                .session(session)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
    }

    private ProductCreateRequest getProductRequest(String name, String description, ProductSellingStatus status) {
        return ProductCreateRequest.builder()
            .name(name)
            .description(description)
            .sellingStatus(status)
            .quickPrice(20000)
            .startBidPrice(1000)
            .minBidPrice(1000)
            .startedAt(LocalDateTime.of(2013,12,18,05,33,35))
            .userId(1L)
            .categoryId(1L)
            .build();
    }
}
