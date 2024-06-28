package com.brokerengine.userservice.account.web.advice.exception;

public class WalletCreationException extends RuntimeException {
    public WalletCreationException(String message) {
        super(message);
    }

    public WalletCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
