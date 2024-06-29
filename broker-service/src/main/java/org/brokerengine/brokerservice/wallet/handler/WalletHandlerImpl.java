package org.brokerengine.brokerservice.wallet.handler;

import org.brokerengine.brokerservice.wallet.entity.Wallet;
import org.brokerengine.brokerservice.wallet.request.CreateWalletRequest;
import org.brokerengine.brokerservice.wallet.request.DepositRequest;
import org.brokerengine.brokerservice.wallet.service.WalletService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class WalletHandlerImpl implements WalletHandler {

    private final WalletService walletService;

    public WalletHandlerImpl(WalletService walletService) {
        this.walletService = walletService;
    }

    public Mono<ServerResponse> createWallet(ServerRequest request) {
        Mono<CreateWalletRequest> body = request.bodyToMono(CreateWalletRequest.class);
        Mono<Wallet> result = body.flatMap(this.walletService::createWallet);
        return serverResponse(result);
    }

    public Mono<ServerResponse> deposit(ServerRequest request) {
        Mono<DepositRequest> body = request.bodyToMono(DepositRequest.class);
        Mono<Wallet> result = body.flatMap(this.walletService::deposit);
        return serverResponse(result);
    }

    public Mono<ServerResponse> getWallet(ServerRequest request) {
        Mono<Wallet> result = Mono.just(request.pathVariable("userId")).flatMap(this.walletService::getWallet);
        return serverResponse(result);
    }

    private Mono<ServerResponse> serverResponse(Mono<Wallet> result) {
        return result.flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }
}