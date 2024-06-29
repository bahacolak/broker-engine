package org.brokerengine.brokerservice.order;

import org.brokerengine.brokerservice.order.handler.OrderHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class OrderRouter {

    private final OrderHandler orderHandler;

    public OrderRouter(OrderHandler orderHandler) {
        this.orderHandler = orderHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> buyOrderRoute() {
        return RouterFunctions.route(POST("/order/buy"), orderHandler::sendBuyOrder);
    }

    @Bean
    public RouterFunction<ServerResponse> sellOrderRoute() {
        return RouterFunctions.route(POST("/order/sell"), orderHandler::sendSellOrder);
    }

    @Bean
    public RouterFunction<ServerResponse> getOrderByIdRoute() {
        return RouterFunctions.route(GET("/order/{orderId}"), orderHandler::getOrderById);
    }

    @Bean
    public RouterFunction<ServerResponse> getOrdersByUserIdRoute() {
        return RouterFunctions.route(GET("/orders/{userId}"), orderHandler::getOrdersByUserId);
    }

    @Bean
    public RouterFunction<ServerResponse> cancelOrderRoute() {
        return RouterFunctions.route(PATCH("/order/cancel/{orderId}"), orderHandler::sendCancelOrder);
    }
}
