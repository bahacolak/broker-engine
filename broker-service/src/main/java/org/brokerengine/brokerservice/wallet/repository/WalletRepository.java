package org.brokerengine.brokerservice.wallet.repository;

import org.brokerengine.brokerservice.wallet.entity.Wallet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface WalletRepository extends ReactiveCrudRepository<Wallet, Long> {
    Mono<Wallet> findByUserId(String userId);
}
