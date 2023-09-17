package com.flab.auctionhub.common.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private final HttpStatus status;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
