package com.flab.auctionhub.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.flab.auctionhub.user.application.request.UserCreateServiceRequest;
import com.flab.auctionhub.user.application.request.UserLoginServiceRequest;
import com.flab.auctionhub.user.application.response.UserCreateResponse;
import com.flab.auctionhub.user.application.response.UserLoginResponse;
import com.flab.auctionhub.user.domain.UserRoleType;
import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import com.flab.auctionhub.user.exception.UserNotFoundException;
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

@ActiveProfiles("test") // 특정 프로파일이 활성화된 상태에서 테스트를 실행할 때 사용되는 어노테이션
@Transactional // 테스트 메서드가 트랜잭션내에서 실행되어, 테스트 수행중에 데이터베이스 조작이 발생하더라도 트랜잭션이 롤백되어 데이터베이스 상태가 변경되지 않게 하는 어노테이션
@SpringBootTest // 스프링 애플리케이션 컨텍스트를 로드하고, 테스트 환경을 설정하여 통합 테스트를 수행하는 어노테이션
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원 1명을 생성하면 userId가 리턴된다.")
    void createUser() {
        // given
        UserCreateServiceRequest userCreateRequest = userCreateRequest("userId", "password", "username", "010-0000-0000");

        // when
        Long userId = userService.createUser(userCreateRequest);

        // then
        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("중복된 회원을 생성하면 예외가 발생한다.")
    void checkUserIdDuplication() {
        // given
        UserCreateServiceRequest userCreateRequest1 = userCreateRequest("userId", "password", "username",
            "010-0000-0000");
        UserCreateServiceRequest userCreateRequest2 = userCreateRequest("userId", "password", "username",
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
        UserCreateServiceRequest userCreateRequest1 = userCreateRequest("userId1", "password1", "username",
            "010-0000-0000");
        UserCreateServiceRequest userCreateRequest2 = userCreateRequest("userId2", "password2", "username",
            "010-1111-1111");
        UserCreateServiceRequest userCreateRequest3 = userCreateRequest("userId3", "password3", "username",
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
        UserCreateServiceRequest userCreateRequest = userCreateRequest("userId1", "password1", "username",
            "010-0000-0000");
        Long userCreateRequestId = userService.createUser(userCreateRequest);

        // when
        UserCreateResponse userCreateResponse = userService.findById(userCreateRequestId);

        // then
        assertThat(userCreateResponse.getUserId()).isEqualTo(userCreateRequest.getUserId());
        assertThat(userCreateResponse.getUsername()).isEqualTo(userCreateRequest.getUsername());
        assertThat(userCreateResponse.getPhoneNumber()).isEqualTo(userCreateRequest.getPhoneNumber());
        assertThat(userCreateResponse.getRoleType()).isEqualTo(UserRoleType.MEMBER);

    }

    @Test
    @DisplayName("회원을 조회하였는데 존재하지 않다면 예외를 발생한다.")
    void findByIdWithoutUser() {
        // when // then
        assertThatThrownBy(() -> userService.findById(1L))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("해당 유저를 찾을 수 없습니다.");
    }

    @ParameterizedTest // JUnit5에서 매개변수화된 테스트를 작성할 때 사용되는 어노테이션
    @JsonSource("""
        [
            {
                userId:'userId',
                password:'password'
            }
        ]
        """) // JSON 형식의 데이터를 매개변수로 제공하는데 사용되는 어노테이션
    @DisplayName("회원을 등록하고 로그인한다.")
    void login(UserLoginServiceRequest userLoginRequest) {
        // given
        UserCreateServiceRequest userCreateRequest = userCreateRequest("userId", "password", "username",
            "010-0000-0000");
        userService.createUser(userCreateRequest);
        MockHttpSession session = new MockHttpSession();

        // when
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest, session);

        // then
        assertThat(userLoginResponse.getUserId()).isEqualTo(userCreateRequest.getUserId());
    }

    private UserCreateServiceRequest userCreateRequest(String userId, String password, String username,
        String phoneNumber) {
        return UserCreateServiceRequest.builder()
            .userId(userId)
            .password(password)
            .username(username)
            .phoneNumber(phoneNumber)
            .build();
    }
}
