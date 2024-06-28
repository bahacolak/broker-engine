package org.brokerengine.brokerservice.wallet.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepositRequest {
    private String userId;
    private Double amount;
}
