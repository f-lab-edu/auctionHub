package com.flab.auctionhub.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.user.application.UserService;
import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입")
    void createUser() throws Exception {
        // given
        UserCreateRequest user = CreateUserInfo1();
        String content = objectMapper.writeValueAsString(user);

        // when
        mockMvc.perform(post("/users")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))

            // then
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("중복된 아이디가 없는지 확인한다.")
    void checkUserIdDuplication() throws Exception {
        // given
        String userId = "testuser";

        // when
        mockMvc.perform(get("/users/check-duplication")
                .param("userId", userId)
                .contentType(MediaType.APPLICATION_JSON))

            // Then
            .andExpect(status().isOk())
            .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("전체 회원을 조회한다.")
    void findAllUser() throws Exception {
        // given
        UserCreateRequest userCreateRequest1 = CreateUserInfo1();
        UserCreateRequest userCreateRequest2 = CreateUserInfo2();

        List<UserCreateRequest> userList = new ArrayList<>();
        userList.add(userCreateRequest1);
        userList.add(userCreateRequest2);

        String content1 = objectMapper.writeValueAsString(userCreateRequest1);
        String content2 = objectMapper.writeValueAsString(userCreateRequest2);

        mockMvc.perform(post("/users")
            .content(content1)
            .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/users")
            .content(content2)
            .contentType(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))

            // then
            .andExpect(status().isOk())

            // then
            .andExpect(jsonPath("$.length()").value(userList.size()))

            .andExpect(jsonPath("$[0].username").value(userCreateRequest1.getUsername()));

        mockMvc.perform(get("/users"))
            .andExpect(jsonPath("$[1].username").value(userCreateRequest2.getUsername()));
    }


    private UserCreateRequest CreateUserInfo1() {
        return UserCreateRequest.builder()
            .userId("userid")
            .password("testpassword")
            .username("testusername")
            .phoneNumber("010-1234-1234")
            .build();
    }

    private UserCreateRequest CreateUserInfo2() {
        return UserCreateRequest.builder()
            .userId("userid2")
            .password("testpassword2")
            .username("testusername2")
            .phoneNumber("010-1234-1235")
            .build();
    }
}
