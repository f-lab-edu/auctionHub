package com.flab.auctionhub.user.api;

import com.flab.auctionhub.user.application.UserService;
import com.flab.auctionhub.user.dto.request.UserCreateRequest;
import com.flab.auctionhub.user.dto.response.UserCreateResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody @Validated UserCreateRequest request) {
        userService.createUser(request);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/users/check-duplication")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestParam String userId) {
        userService.checkUserIdDuplication(userId);
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserCreateResponse>> findAllUser() {
        List<UserCreateResponse> userList = userService.findAllUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserCreateResponse> findByIdUser(@PathVariable Long id) {
        UserCreateResponse user = userService.findByIdUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
