package com.flab.auctionhub.common.exception;

public class HttpSessionNotFoundException extends RuntimeException {

    public HttpSessionNotFoundException(String message) {
        super(message);
    }
}
