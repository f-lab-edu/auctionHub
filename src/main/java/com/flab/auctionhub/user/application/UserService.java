package com.flab.auctionhub.user.application;

import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import com.flab.auctionhub.user.dto.request.UserLoginRequest;
import com.flab.auctionhub.user.dto.response.UserCreateResponse;
import com.flab.auctionhub.user.dto.response.UserLoginResponse;
import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import com.flab.auctionhub.user.exception.InvalidSigningInformationException;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 서비스 레이어임을 명시해주며 @Component를 가지고 있어 스프링 빈으로 등록 시키는 역할을 수행한다.
@RequiredArgsConstructor
public class UserService {

    private static final String USER_ID = "USER_ID";
    private final UserMapper userMapper;

    @Transactional // 수행하는 작업에 대해 트랜잭션 원칙이 지켜지도록 보장해주는 역할을 한다.
    public Long createUser(UserCreateRequest request) {
        this.checkUserIdDuplication(request.getUserId());
        User user = request.toEntity();
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

    public UserLoginResponse login(UserLoginRequest request, HttpSession session) {
        User user = userMapper.findByUserIdAndPassword(request.getUserId(), request.getPassword())
            .orElseThrow(() -> new InvalidSigningInformationException("아이디/비밀번호가 올바르지 않습니다."));
        session.setAttribute(USER_ID, user.getUserId());
        return UserLoginResponse.of(user);
    }
}
