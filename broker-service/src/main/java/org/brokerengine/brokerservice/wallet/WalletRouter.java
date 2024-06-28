package org.brokerengine.brokerservice.wallet;

import org.brokerengine.brokerservice.wallet.handler.WalletHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration(proxyBeanMethods = false)
public class WalletRouter {

    private final WalletHandler walletHandler;

    public WalletRouter(WalletHandler walletHandler) {
        this.walletHandler = walletHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> createWalletRoute() {
        return RouterFunctions.route(POST("/wallet"), walletHandler::createWallet);
    }

    @Bean
    public RouterFunction<ServerResponse> getWalletRoute() {
        return RouterFunctions.route(GET("/wallet/{userId}"), walletHandler::getWallet);
    }

    @Bean
    public RouterFunction<ServerResponse> depositRoute() {
        return RouterFunctions.route(POST("/wallet/deposit"), walletHandler::deposit);
    }
}
