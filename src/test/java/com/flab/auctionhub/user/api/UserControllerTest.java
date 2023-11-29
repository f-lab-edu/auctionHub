package com.flab.auctionhub.user.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.auctionhub.user.application.UserService;
import com.flab.auctionhub.user.domain.UserRoleType;
import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import com.flab.auctionhub.user.dto.request.UserLoginRequest;
import com.flab.auctionhub.user.dto.response.UserCreateResponse;
import com.flab.auctionhub.user.dto.response.UserLoginResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(UserController.class) // Spring MVC 애플리케이션을 테스트하기 위한 목적으로 해당 계층을 중심으로 필요한 빈들을 로드하여 테스트 수행하는데 사용되는 어노테이션
class UserControllerTest {

    @Autowired // 스프링 프레임워크에서 의존성 주입을 위해 사용되는 어노테이션
    private MockMvc mockMvc;

    @MockBean // 특정 빈을 테스트에서 의존하지 않도록 가짜 빈을 생성해주는 어노테이션
    private UserService userService;

    @Autowired // 의존 객체의 타입에 해당하는 빈을 찾아 주입하는 어노테이션
    private ObjectMapper objectMapper;

    @Test // JUnit 에서 테스트 메서드를 식별하기 위해 사용되는 어노테이션
    @DisplayName("신규 회원을 등록한다.") // JUnit5 에서 해당 테스트의 이름을 좀 더 의미 있게 정의하기 위해 사용되는 어노테이션
    void createUser() throws Exception {
        // given
        UserCreateRequest user = CreateUserInfo1();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value(0L));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 회원 아이디는 필수 값이다.")
    void createUserWithoutUserId() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .password("testpassword")
            .username("testusername")
            .phoneNumber("010-1234-1234")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("아이디 입력은 필수입니다."));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 회원 비밀번호는 필수 값이다.")
    void createUserWithoutPassword() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .userId("userid")
            .username("testusername")
            .phoneNumber("010-1234-1234")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("비밀번호 입력은 필수입니다."));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 회원명은 필수 값이다.")
    void createUserWithoutUsername() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .userId("userid")
            .password("testpassword")
            .phoneNumber("010-1234-1234")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("회원명 입력은 필수입니다."));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 휴대폰 번호는 필수 값이다.")
    void createUserWithoutPhoneNumber() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .userId("userid")
            .password("testpassword")
            .username("testusername")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("휴대폰 번호 입력은 필수입니다."));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 userId 값은 6자리 이상 12자 이하여야 한다.")
    void createUserCheckUserIdSize() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .userId("id")
            .password("testpassword")
            .username("testusername")
            .phoneNumber("010-1234-1234")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("아이디는 6자리 이상 12자 이하여야 합니다."));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 password 값은 8자리 이상 16자 이하여야 한다.")
    void createUserCheckPasswordSize() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .userId("userid")
            .password("testpassword1234567890")
            .username("testusername")
            .phoneNumber("010-1234-1234")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("비밀번호는 8자 이상 16자 이하여야 합니다."));
    }

    @Test
    @DisplayName("신규 회원을 등록할 때 휴대폰 번호는 규격에 맞게 작성해야 한다.")
    void createUserCheckPhoneNumberPattern() throws Exception {
        // given
        UserCreateRequest user = UserCreateRequest.builder()
            .userId("userid")
            .password("testpassword")
            .username("testusername")
            .phoneNumber("01012341234")
            .build();
        userService.createUser(user);

        // when // then
        mockMvc.perform(
                post("/users")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("휴대폰 번호 형식에 맞게 입력해 주세요."));
    }

    @Test
    @DisplayName("중복된 아이디가 없는지 확인한다.")
    void checkUserIdDuplication() throws Exception {
        // given
        String userId = "testuser";

        // when // Then
        mockMvc.perform(
                get("/users/check-duplication")
                    .param("userId", userId)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("전체 회원을 조회한다.")
    void findAllUser() throws Exception {
        // given
        List<UserCreateResponse> result = List.of();

        when(userService.findAllUser()).thenReturn(result);

        // when // then
        mockMvc.perform(
                get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("id를 이용하여 회원을 조회한다.")
    void findById() throws Exception {
        // given
        UserCreateRequest userCreateRequest = CreateUserInfo1();
        Long id = userService.createUser(userCreateRequest);
        UserCreateResponse userCreateResponse = new UserCreateResponse(
            userCreateRequest.getUserId(), userCreateRequest.getUsername(), UserRoleType.MEMBER,
            userCreateRequest.getPhoneNumber());
        when(userService.findById(id)).thenReturn(userCreateResponse);

        // when // then
        mockMvc.perform(
                get("/users/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 로그인을 한다.")
    void login() throws Exception {
        // given
        UserCreateRequest userCreateRequest = CreateUserInfo1();
        userService.createUser(userCreateRequest);
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
            .userId(userCreateRequest.getUserId())
            .password(userCreateRequest.getPassword())
            .build();
        MockHttpSession session = new MockHttpSession();
        UserLoginResponse userLoginResponse = new UserLoginResponse(
            userCreateRequest.getUserId(), userCreateRequest.getPassword(),
            userCreateRequest.getUsername(), UserRoleType.MEMBER,
            userCreateRequest.getPhoneNumber());

        when(userService.login(userLoginRequest, session)).thenReturn(userLoginResponse);

        // when // then
        mockMvc.perform(
                post("/login")
                    .session(session)
                    .content(objectMapper.writeValueAsString(userLoginRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 로그인시 회원의 아이디는 필수 값이다.")
    void loginWithoutUserId() throws Exception {
        // given
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
            .password("password")
            .build();
        MockHttpSession session = new MockHttpSession();

        // when // then
        mockMvc.perform(
                post("/login")
                    .session(session)
                    .content(objectMapper.writeValueAsString(userLoginRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("아이디 입력은 필수입니다."));
    }

    @Test
    @DisplayName("회원 로그인시 회원의 비밀번호는 필수 값이다.")
    void loginWithoutPassword() throws Exception {
        // given
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
            .userId("userId")
            .build();
        MockHttpSession session = new MockHttpSession();

        // when // then
        mockMvc.perform(
                post("/login")
                    .session(session)
                    .content(objectMapper.writeValueAsString(userLoginRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("비밀번호 입력은 필수입니다."));
    }

    @Test
    @DisplayName("회원 로그아웃을 한다.")
    void logoutUser() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        // when // then
        mockMvc.perform(
                get("/logout")
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    private UserCreateRequest CreateUserInfo1() {
        return UserCreateRequest.builder()
            .userId("userid")
            .password("testpassword")
            .username("testusername")
            .phoneNumber("010-1234-1234")
            .build();
    }
}
