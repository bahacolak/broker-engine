package org.brokerengine.brokerservice.order.service;

import org.brokerengine.brokerservice.order.entity.StockOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<StockOrder> getOrderById(String orderId);
    Flux<StockOrder> getOrdersByUserId(String userId);
}
