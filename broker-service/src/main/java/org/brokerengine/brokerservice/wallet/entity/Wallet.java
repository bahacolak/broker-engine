package org.brokerengine.brokerservice.wallet.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Wallet {
    @Id
    private Long id;
    private String userId;
    private Integer stockCount;
    private Integer stockCountOnHold;
    private Double balance;
    private Double balanceOnHold;

    public Wallet(String userId, Integer stockCount, Integer stockCountOnHold, Double balance, Double balanceOnHold) {
        this.userId = userId;
        this.stockCount = stockCount;
        this.stockCountOnHold = stockCountOnHold;
        this. balance = balance;
        this.balanceOnHold = balanceOnHold;
    }
}
