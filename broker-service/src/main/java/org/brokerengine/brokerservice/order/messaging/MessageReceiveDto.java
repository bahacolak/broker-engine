package org.brokerengine.brokerservice.order.messaging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReceiveDto {
    private String orderId;
    private String orderType;
    private String status;
}
