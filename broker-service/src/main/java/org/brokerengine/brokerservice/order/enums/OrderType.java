package org.brokerengine.brokerservice.order.enums;

public enum OrderType {
    BUY("buy"),
    SELL("sell"),
    CANCEL("cancel");

    public final String label;

    OrderType(String label) {
        this.label = label;
    }
}
