package com.flab.auctionhub.user.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.domain.UserRoleType;
import com.flab.auctionhub.user.exception.InvalidSigningInformationException;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    @DisplayName("회원 가입 테스트를 한다.")
    void save() {
        // given
        User user = getUser("userId", "username", "010-0000-0000");

        // when
        userMapper.save(user);

        User result = userMapper.findByUserId(user.getUserId())
            .orElseThrow(() -> new InvalidSigningInformationException("등록 되지 않은 사용자입니다."));

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo(user.getUserId());
        assertThat(result.getPassword()).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(result.getRoleType()).isEqualTo(UserRoleType.MEMBER);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedBy()).isEqualTo(user.getUserId());
        assertThat(result.getUpdatedAt()).isNull();
        assertThat(result.getUpdatedBy()).isNull();
    }

    @Test
    @DisplayName("회원 존재 여부를 테스트 한다.")
    void isExistUserId() {
        // given
        User user = getUser("userId", "username", "010-0000-0000");
        userMapper.save(user);

        // when
        boolean result = userMapper.isExistUserId(user.getUserId());
        boolean result2 = userMapper.isExistUserId("");

        // then
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("전체 회원 목록을 테스트 한다.")
    void findAll() {
        // given
        User user1 = getUser("userId1", "username1", "010-1111-1111");
        User user2 = getUser("userId2", "username2", "010-2222-2222");
        User user3 = getUser("userId3", "username3", "010-3333-3333");
        userMapper.save(user1);
        userMapper.save(user2);
        userMapper.save(user3);

        // when
        List<User> userList = userMapper.findAll();

        // then
        assertThat(userList).hasSize(3)
            .extracting("userId", "username", "phoneNumber")
            .containsExactlyInAnyOrder(
                tuple(user1.getUserId(), user1.getUsername(), user1.getPhoneNumber()),
                tuple(user2.getUserId(), user2.getUsername(), user2.getPhoneNumber()),
                tuple(user3.getUserId(), user3.getUsername(), user3.getPhoneNumber())
            );
    }

    @Test
    @DisplayName("id를 이용하여 회원을 조회한다.")
    void findById() {
        // given
        User user = getUser("userId", "username", "010-0000-0000");
        userMapper.save(user);

        // when
        User result = userMapper.findById(user.getId())
            .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        // then
        assertThat(user.getUserId()).isEqualTo(result.getUserId());
        assertThat(user.getUsername()).isEqualTo(result.getUsername());
        assertThat(user.getPhoneNumber()).isEqualTo(result.getPhoneNumber());
    }

    @Test
    @DisplayName("userId를 이용하여 회원을 조회한다.")
    void findByUserId() {
        // given
        User user = getUser("userId", "username", "010-0000-0000");
        userMapper.save(user);

        // when
        User result = userMapper.findByUserId(user.getUserId())
            .orElseThrow(() -> new InvalidSigningInformationException("등록 되지 않은 사용자입니다."));

        // then
        assertThat(user.getUserId()).isEqualTo(result.getUserId());
        assertThat(user.getUsername()).isEqualTo(result.getUsername());
        assertThat(user.getPhoneNumber()).isEqualTo(result.getPhoneNumber());
    }

    private User getUser(String userId, String username, String phoneNumber) {
        return User.builder()
            .userId(userId)
            .password("testpassword")
            .username(username)
            .phoneNumber(phoneNumber)
            .build();
    }
}
