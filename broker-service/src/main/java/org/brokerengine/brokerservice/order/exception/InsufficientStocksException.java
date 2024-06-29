package org.brokerengine.brokerservice.order.exception;

public class InsufficientStocksException extends Exception {
    public InsufficientStocksException() {
        super("Stock number in wallet is lower what was ordered for selling!");
    }
}
