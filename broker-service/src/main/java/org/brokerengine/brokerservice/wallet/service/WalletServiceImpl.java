package org.brokerengine.brokerservice.wallet.service;

import org.brokerengine.brokerservice.wallet.entity.Wallet;
import org.brokerengine.brokerservice.wallet.exception.WalletNotFoundException;
import org.brokerengine.brokerservice.wallet.repository.WalletRepository;
import org.brokerengine.brokerservice.wallet.request.CreateWalletRequest;
import org.brokerengine.brokerservice.wallet.request.DepositRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements WalletService {
    WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Mono<Wallet> createWallet(CreateWalletRequest createWalletRequest) {
        return walletRepository.save(new Wallet(createWalletRequest.getUserId(), 0, 0, 0.0, 0.0));
    }

    @Override
    public Mono<Wallet> deposit(DepositRequest depositRequest) {
        String userId = depositRequest.getUserId();
        return walletRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException()))
                .flatMap(w -> {
                    w.setBalance(w.getBalance() + depositRequest.getAmount());
                    return walletRepository.save(w);
                });
    }

    @Override
    public Mono<Wallet> getWallet(String userId) {
        return walletRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException()));
    }
}
