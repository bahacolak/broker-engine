package org.brokerengine.brokerservice.order.strategy;

public interface OrderStrategy {
    void adjustWallet(String orderId);
}
