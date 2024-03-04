package com.flab.auctionhub.order.exception;

public class WrongOrderStatusException extends RuntimeException {

    public WrongOrderStatusException(String message) {
        super(message);
    }
}
