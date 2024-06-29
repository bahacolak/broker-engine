package org.brokerengine.brokerservice.order.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Balance is lower than the requested order amount!");
    }
}
