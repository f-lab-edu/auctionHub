package com.flab.auctionhub.category.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.category.application.CategoryService;
import com.flab.auctionhub.category.domain.CategoryType;
import com.flab.auctionhub.category.api.request.CategoryRequest;
import com.flab.auctionhub.category.application.response.CategoryResponse;
import com.flab.auctionhub.common.util.SessionUtil;
import com.flab.auctionhub.user.domain.UserRoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("신규 카테고리를 등록한다.")
    void createCategory() throws Exception {
        // given
        CategoryRequest request = CategoryRequest.builder()
            .name(CategoryType.BAG)
            .build();

        MockHttpSession session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.ADMIN);


        // when // then
        mockMvc.perform(
                post("/category")
                    .session(session)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("신규 카테고리를 등록할 때 카테고리 타입은 필수 값이다.")
    void createCategoryWithoutType() throws Exception {
        // given
        CategoryRequest request = CategoryRequest.builder()
            .build();

        MockHttpSession session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.ADMIN);

        // when // then
        mockMvc.perform(
                post("/category")
                    .session(session)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 타입은 필수입니다."));
    }


    @Test
    @DisplayName("전체 카테고리를 조회한다.")
    void findAllCategory() throws Exception {
        // given
        List<CategoryResponse> result = List.of();

        MockHttpSession session = new MockHttpSession();
        SessionUtil.setLoginUserId(session, "USER_ID");
        SessionUtil.setLoginUserRole(session, UserRoleType.MEMBER);

        when(categoryService.findAllCategory()).thenReturn(result);

        // when // then
        mockMvc.perform(
                get("/category")
                    .session(session)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
