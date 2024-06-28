package org.brokerengine.brokerservice.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    private Long id;
    private String userId;
    private Integer stockCount;
    private Integer stockCountOnHold;
    private Double balance;
    private Double balanceOnHold;

}
