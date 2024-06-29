package org.brokerengine.brokerservice.order.handler;

import org.brokerengine.brokerservice.order.entity.CancelOrder;
import org.brokerengine.brokerservice.order.entity.StockOrder;
import org.brokerengine.brokerservice.order.request.OrderRequest;
import org.brokerengine.brokerservice.order.service.OrderService;
import org.brokerengine.brokerservice.order.service.SendOrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderHandlerImpl implements OrderHandler {
    private final SendOrderService sendOrderService;
    private final OrderService orderService;

    public OrderHandlerImpl(SendOrderService sendOrderService, OrderService orderService) {
        this.sendOrderService = sendOrderService;
        this.orderService = orderService;
    }

    @Override
    public Mono<ServerResponse> sendBuyOrder(ServerRequest request) {
        Mono<OrderRequest> body = request.bodyToMono(OrderRequest.class);
        Mono<StockOrder> result = body.flatMap(this.sendOrderService::sendBuyOrder);
        return result.flatMap(data -> ServerResponse.accepted().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    @Override
    public Mono<ServerResponse> sendSellOrder(ServerRequest request) {
        Mono<OrderRequest> body = request.bodyToMono(OrderRequest.class);
        Mono<StockOrder> result = body.flatMap(this.sendOrderService::sendSellOrder);
        return result.flatMap(data -> ServerResponse.accepted().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    @Override
    public Mono<ServerResponse> sendCancelOrder(ServerRequest request) {
        Mono<CancelOrder> result = sendOrderService.sendCancelOrder(request.pathVariable("orderId"));
        return result.flatMap(data -> ServerResponse.accepted().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    @Override
    public Mono<ServerResponse> getOrderById(ServerRequest request) {
        Mono<StockOrder> result = Mono.just(request.pathVariable("orderId")).flatMap(this.orderService::getOrderById);
        return result.flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    @Override
    public Mono<ServerResponse> getOrdersByUserId(ServerRequest request) {
        Flux<StockOrder> stockOrderFlux = orderService.getOrdersByUserId(request.pathVariable("userId"));
        return ServerResponse.ok().body(stockOrderFlux, StockOrder.class);
    }
}
