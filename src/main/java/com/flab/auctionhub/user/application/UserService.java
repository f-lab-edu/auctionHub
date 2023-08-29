package com.flab.auctionhub.user.application;

import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import com.flab.auctionhub.user.dto.response.UserCreateResponse;
import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Transactional
    public void createUser(UserCreateRequest request) {
        this.checkUserIdDuplication(request.getUserId());
        userMapper.save(request.toEntity());
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

    public UserCreateResponse findByIdUser(Long id) {
        return userMapper.findById(id)
            .map(UserCreateResponse::of)
            .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
    }
}
