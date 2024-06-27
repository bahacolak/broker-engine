package com.brokerengine.userservice.account.web.advice.exception;

public class UserConflictException extends RuntimeException{

    public UserConflictException(String message) {
        super(message);
    }
}
