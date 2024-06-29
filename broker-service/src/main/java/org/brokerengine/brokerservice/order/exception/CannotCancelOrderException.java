package org.brokerengine.brokerservice.order.exception;

public class CannotCancelOrderException extends Exception {
    public CannotCancelOrderException() {
        super("Order can not be canceled because it is either done, failed or canceled");
    }
}
