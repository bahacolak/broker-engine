package org.brokerengine.brokerservice.wallet.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateWalletRequest {
    private String userId;
}
