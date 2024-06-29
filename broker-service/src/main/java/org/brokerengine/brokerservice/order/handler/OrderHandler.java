package org.brokerengine.brokerservice.order.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface OrderHandler {
    Mono<ServerResponse> sendBuyOrder(ServerRequest request);
    Mono<ServerResponse> sendSellOrder(ServerRequest request);
    Mono<ServerResponse> sendCancelOrder(ServerRequest request);
    Mono<ServerResponse> getOrderById(ServerRequest request);
    Mono<ServerResponse> getOrdersByUserId(ServerRequest request);
}
