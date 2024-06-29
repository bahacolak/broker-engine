package org.brokerengine.brokerservice.order.service;

import org.brokerengine.brokerservice.order.entity.StockOrder;
import org.brokerengine.brokerservice.order.exception.OrderNotFoundException;
import org.brokerengine.brokerservice.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Mono<StockOrder> getOrderById(String orderId) {
        return orderRepository.findById(Long.valueOf(orderId))
                .switchIfEmpty(Mono.error(new OrderNotFoundException()));
    }

    @Override
    public Flux<StockOrder> getOrdersByUserId(String userId) {
        return orderRepository.findStockOrdersByUserId(userId);
    }
}
