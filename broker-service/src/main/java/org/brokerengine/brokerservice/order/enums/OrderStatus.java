package org.brokerengine.brokerservice.order.enums;

public enum OrderStatus {
    DONE("done"),
    FAILED("failed"),
    PROCESSING("processing"),
    CANCELED("canceled");

    public final String label;

    OrderStatus(String label) {
        this.label = label;
    }
}
