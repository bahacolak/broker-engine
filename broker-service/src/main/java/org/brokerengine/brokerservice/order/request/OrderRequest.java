package org.brokerengine.brokerservice.order.request;

import lombok.Getter;

@Getter
public class OrderRequest {
    private String userId;
    private Integer numberOfShares;
    private Double price;
}
