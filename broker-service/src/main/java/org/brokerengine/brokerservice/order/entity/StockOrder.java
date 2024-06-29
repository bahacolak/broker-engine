package org.brokerengine.brokerservice.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class StockOrder {
    @Id
    private Long id;
    private String userId;
    private Integer numberOfShares;
    private Double price;
    private String orderStatus;
    private String orderType;

    public StockOrder(String userId, Integer numberOfShares, Double price, String orderStatus, String orderType) {
        this.userId = userId;
        this.numberOfShares = numberOfShares;
        this.price = price;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
    }
}
