package com.flab.auctionhub.user.api;

import com.flab.auctionhub.user.application.UserService;
import com.flab.auctionhub.user.api.request.UserCreateRequest;
import com.flab.auctionhub.user.api.request.UserLoginRequest;
import com.flab.auctionhub.user.application.response.UserCreateResponse;
import java.util.List;
import com.flab.auctionhub.user.application.response.UserLoginResponse;
import jakarta.servlet.http.HttpSession;
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

@RestController // @Controller + @ResponseBody 어노테이션으로 JSON 형태로 객체 데이터를 반환한다.
@RequiredArgsConstructor // lombok에서 제공하는 어노테이션으로 final 키워드 또는 @NonNull이 붙은 필드들로 이루어진 생성자를 만들어준다.
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    // HTTP POST 요청을 매핑하는 어노테이션이며 @RequestMapping(method = RequestMethod.POST)과 같은 역할을 한다.
    public ResponseEntity<Long> createUser(@RequestBody @Validated UserCreateRequest request) {
        // @RequestBody : 요청의 body 데이터를 객체로 변환해주는 어노테이션, @Validated : 유효성 검증을 적용하기 위해 사용된 어노테이션
        Long id = userService.createUser(request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/users/check-duplication")
    // HTTP GET 요청을 매핑하는 어노테이션이며 @RequestMapping(method = RequestMethod.GET)과 같은 역할을 한다.
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestParam String userId) {
        userService.checkUserIdDuplication(userId);
        return ResponseEntity.status(HttpStatus.OK).body(Boolean.FALSE);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserCreateResponse>> findAllUser() {
        List<UserCreateResponse> userList = userService.findAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserCreateResponse> findById(@PathVariable Long id) {
        UserCreateResponse user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Validated UserLoginRequest request,
        HttpSession session) {
        UserLoginResponse userLoginResponse = userService.login(request.toServiceRequest(), session);
        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
