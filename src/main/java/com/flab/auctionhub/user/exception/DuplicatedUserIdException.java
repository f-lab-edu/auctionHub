package com.flab.auctionhub.user.exception;

public class DuplicatedUserIdException extends RuntimeException {

    public DuplicatedUserIdException(String message) {
        super(message);
    }
}
