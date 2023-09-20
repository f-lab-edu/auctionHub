package com.flab.auctionhub.user.exception;

public class WrongRoleCodeException extends RuntimeException {

    public WrongRoleCodeException(String message) {
        super(message);
    }
}
