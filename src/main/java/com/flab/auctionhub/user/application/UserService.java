package com.flab.auctionhub.user.application;

import com.flab.auctionhub.user.dao.UserMapper;
import com.flab.auctionhub.user.domain.User;
import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import com.flab.auctionhub.user.dto.response.UserCreateResponse;
import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Transactional
    public void createUser(UserCreateRequest request) {
        User user = User.builder()
            .userId(request.getUserId())
            .password(request.getPassword())
            .username(request.getUsername())
            .phoneNumber(request.getPhoneNumber())
            .createdBy(request.getUsername())
            .build();
        userMapper.save(user);
    }

    public void checkUserIdDuplication(String userId) {
        if (userMapper.isExistUserId(userId)) {
            throw new DuplicatedUserIdException("중복된 아이디입니다.");
        }
    }

    public List<UserCreateResponse> findAllUser() {
        List<User> users = userMapper.findAll();
        List<UserCreateResponse> userCreateResponseList = new ArrayList<>();

        for (User user : users) {
            UserCreateResponse userCreateResponse = UserCreateResponse.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .username(user.getUsername())
                .roleType(user.getRoleType())
                .phoneNumber(user.getPhoneNumber())
                .build();
            userCreateResponseList.add(userCreateResponse);
        }
        return userCreateResponseList;
    }

    public UserCreateResponse findByIdUser(Long id) {
        User user = userMapper.findById(id).orElseThrow(NullPointerException::new);
        return UserCreateResponse.builder()
            .userId(user.getUserId())
            .password(user.getPassword())
            .username(user.getUsername())
            .roleType(user.getRoleType())
            .phoneNumber(user.getPhoneNumber())
            .build();
    }
}
