package org.brokerengine.brokerservice.order.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException() {
        super("No Order is found for given orderId");
    }
}
