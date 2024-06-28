package org.brokerengine.brokerservice.wallet.service;

import org.brokerengine.brokerservice.wallet.entity.Wallet;
import org.brokerengine.brokerservice.wallet.request.CreateWalletRequest;
import org.brokerengine.brokerservice.wallet.request.DepositRequest;
import reactor.core.publisher.Mono;

public interface WalletService {
    Mono<Wallet> createWallet(CreateWalletRequest createWalletRequest);
    Mono<Wallet> deposit(DepositRequest depositRequest);
    Mono<Wallet> getWallet(String userId);
}
