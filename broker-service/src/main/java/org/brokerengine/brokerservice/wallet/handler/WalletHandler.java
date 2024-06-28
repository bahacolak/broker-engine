package org.brokerengine.brokerservice.wallet.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface WalletHandler {
    Mono<ServerResponse> createWallet(ServerRequest serverRequest);
    Mono<ServerResponse> deposit(ServerRequest serverRequest);
    Mono<ServerResponse> getWallet(ServerRequest serverRequest);
}
