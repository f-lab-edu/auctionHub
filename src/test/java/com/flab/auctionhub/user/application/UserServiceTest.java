package com.flab.auctionhub.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import com.flab.auctionhub.user.dto.request.UserLoginRequest;
import com.flab.auctionhub.user.dto.response.UserCreateResponse;
import com.flab.auctionhub.user.dto.response.UserLoginResponse;
import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원 1명을 생성하면 userId가 리턴된다.")
    void createUser() {
        // given
        UserCreateRequest userCreateRequest = userCreateRequest("userId", "password", "username", "010-0000-0000");

        // when
        Long userId = userService.createUser(userCreateRequest);

        // then
        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("중복된 회원을 생성하면 예외가 발생한다.")
    void checkUserIdDuplication() {
        // given
        UserCreateRequest userCreateRequest1 = userCreateRequest("userId", "password", "username",
            "010-0000-0000");
        UserCreateRequest userCreateRequest2 = userCreateRequest("userId", "password", "username",
            "010-0000-0000");
        userService.createUser(userCreateRequest1);

        // when // then
        assertThatThrownBy(() -> userService.createUser(userCreateRequest2))
            .isInstanceOf(DuplicatedUserIdException.class)
            .hasMessage("중복된 아이디입니다.");
    }

    @Test
    @DisplayName("회원 3명을 생성하여 전체 회원을 조회한다.")
    void findAllUser() {
        // given
        UserCreateRequest userCreateRequest1 = userCreateRequest("userId1", "password1", "username",
            "010-0000-0000");
        UserCreateRequest userCreateRequest2 = userCreateRequest("userId2", "password2", "username",
            "010-1111-1111");
        UserCreateRequest userCreateRequest3 = userCreateRequest("userId3", "password3", "username",
            "010-2222-2222");
        userService.createUser(userCreateRequest1);
        userService.createUser(userCreateRequest2);
        userService.createUser(userCreateRequest3);

        // when
        List<UserCreateResponse> userList = userService.findAllUser();

        // then
        assertThat(userList).hasSize(3)
            .extracting("userId", "username", "phoneNumber")
            .containsExactlyInAnyOrder(
                tuple(userCreateRequest1.getUserId(), userCreateRequest1.getUsername(),
                    userCreateRequest1.getPhoneNumber()),
                tuple(userCreateRequest2.getUserId(), userCreateRequest2.getUsername(),
                    userCreateRequest2.getPhoneNumber()),
                tuple(userCreateRequest3.getUserId(), userCreateRequest3.getUsername(),
                    userCreateRequest3.getPhoneNumber())
            );
    }

    @Test
    @DisplayName("회원을 등록하고 PK값인 id를 이용하여 회원을 조회한다.")
    void findById() {
        // given
        UserCreateRequest userCreateRequest = userCreateRequest("userId1", "password1", "username",
            "010-0000-0000");
        Long userCreateRequestId = userService.createUser(userCreateRequest);

        // when
        UserCreateResponse userCreateResponse = userService.findById(userCreateRequestId);

        // then
        assertThat(userCreateResponse.getUserId()).isEqualTo(userCreateRequest.getUserId());
        assertThat(userCreateResponse.getUsername()).isEqualTo(userCreateRequest.getUsername());
        assertThat(userCreateResponse.getPhoneNumber()).isEqualTo(
            userCreateRequest.getPhoneNumber());
    }

    @ParameterizedTest
    @JsonSource("""
        [
            {
                userId:'userId',
                password:'password'
            }
        ]
        """)
    @DisplayName("회원을 등록하고 로그인한다.")
    void login(UserLoginRequest userLoginRequest) {
        // given
        UserCreateRequest userCreateRequest = userCreateRequest("userId", "password", "username",
            "010-0000-0000");
        userService.createUser(userCreateRequest);
        MockHttpSession session = new MockHttpSession();

        // when
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest, session);

        // then
        assertThat(userLoginResponse.getUserId()).isEqualTo(userCreateRequest.getUserId());
    }

    private UserCreateRequest userCreateRequest(String userId, String password, String username,
        String phoneNumber) {
        return UserCreateRequest.builder()
            .userId(userId)
            .password(password)
            .username(username)
            .phoneNumber(phoneNumber)
            .build();
    }
}
