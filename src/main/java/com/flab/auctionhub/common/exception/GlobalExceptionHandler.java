package com.flab.auctionhub.common.exception;

import com.flab.auctionhub.user.exception.DuplicatedUserIdException;
import com.flab.auctionhub.user.exception.UserNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j // 로깅을 쉽게 구현할 수 있도록 도와주는 롬복 어노테이션
@RestControllerAdvice // @ControllerAdvice + @ResponseBody 모든 컨트롤러에 대한, 전역적으로 적용되는 예외 처리를 담당하는 클래스를 정의할 때 사용한다.
public class GlobalExceptionHandler {

    private static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private ResponseEntity<ErrorResponse> buildAndReturnResponse(HttpStatus status, String message) {
        String currentTime = getCurrentTime();
        log.error("[ Error ] {} : {}", currentTime, message);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(message)
            .status(status)
            .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedUserIdException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedUserIdException(DuplicatedUserIdException exception) {
        return buildAndReturnResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return buildAndReturnResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}