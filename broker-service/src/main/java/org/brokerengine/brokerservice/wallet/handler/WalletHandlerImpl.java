package org.brokerengine.brokerservice.wallet.handler;

import org.brokerengine.brokerservice.wallet.entity.Wallet;
import org.brokerengine.brokerservice.wallet.request.CreateWalletRequest;
import org.brokerengine.brokerservice.wallet.request.DepositRequest;
import org.brokerengine.brokerservice.wallet.service.WalletService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class WalletHandlerImpl implements WalletHandler{

    private final WalletService walletService;

    public WalletHandlerImpl(WalletService walletService) {
        this.walletService = walletService;
    }

    public Mono<ServerResponse> createWallet(ServerRequest serverRequest) {
        Mono<CreateWalletRequest> body = serverRequest.bodyToMono(CreateWalletRequest.class);
        Mono<Wallet> result = body.flatMap(this.walletService::createWallet);
        return ServerResponse(result);
    }

    public Mono<ServerResponse> deposit(ServerRequest serverRequest) {
        Mono<DepositRequest> body = serverRequest.bodyToMono(DepositRequest.class);
        Mono<Wallet> result = body.flatMap(this.walletService::deposit);
        return ServerResponse(result);
    }

    public Mono<ServerResponse> getWallet(ServerRequest serverRequest) {
        Mono<Wallet> result = Mono.just(serverRequest.pathVariable("userId")).flatMap(this.walletService::getWallet);
        return ServerResponse(result);