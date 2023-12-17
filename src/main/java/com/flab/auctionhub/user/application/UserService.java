package com.flab.auctionhub.user.application;

import com.flab.auctionhub.user.application.request.UserCreateServiceRequest;
import com.flab.auctionhub.user.application.request.UserLoginServiceRequest;
import com.flab.auctionhub.user.application.response.UserCreateResponse;
import com.flab.auctionhub.user.application.response.UserLoginResponse;
import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import com.flab.auctionhub.user.exception.InvalidSigningInformationException;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 서비스 레이어임을 명시해주며 @Component를 가지고 있어 스프링 빈으로 등록 시키는 역할을 수행한다.
@Transactional(readOnly = true) // readOnly = true : 읽기 전용, CRUD에서 CUD 동작X / only Read
@RequiredArgsConstructor
public class UserService {

    private static final String USER_ID = "USER_ID";
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional // 수행하는 작업에 대해 트랜잭션 원칙이 지켜지도록 보장해주는 역할을 한다.
    public Long createUser(UserCreateServiceRequest request) {
        this.checkUserIdDuplication(request.getUserId());
        User user = request.toEntity(passwordEncoder);
        userMapper.save(user);
        return user.getId();
    }

    public void checkUserIdDuplication(String userId) {
        if (userMapper.isExistUserId(userId)) {
            throw new DuplicatedUserIdException("중복된 아이디입니다.");
        }
    }

    public List<UserCreateResponse> findAllUser() {
        return userMapper.findAll().stream()
            .map(UserCreateResponse::of)
            .collect(Collectors.toList());
    }

    public UserCreateResponse findById(Long id) {
        return userMapper.findById(id)
            .map(UserCreateResponse::of)
            .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    public UserLoginResponse login(UserLoginServiceRequest request, HttpSession session) {
        User user = userMapper.findByUserId(request.getUserId())
            .orElseThrow(() -> new InvalidSigningInformationException("등록 되지 않은 사용자입니다."));

        if (validatePassword(user.getPassword(), request.getPassword())) {
            throw new InvalidSigningInformationException("비밀번호가 올바르지 않습니다.");
        }

        session.setAttribute(USER_ID, user.getUserId());
        return UserLoginResponse.of(user);
    }

    private boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
