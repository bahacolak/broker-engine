package org.brokerengine.brokerservice.order.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class MessageSendDto {
    private String orderId;
    private String orderType;
    private Integer numberOfShares;
    private Double price;
}
