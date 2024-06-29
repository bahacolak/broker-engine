package org.brokerengine.brokerservice.order.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class CancelOrder {
    @Id
    private Long id;
    private String canceledOrderId;

    public CancelOrder(String canceledOrderId) {
        this.canceledOrderId = canceledOrderId;
    }
}
