package org.brokerengine.brokerservice.order.repository;

import org.brokerengine.brokerservice.order.entity.StockOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<StockOrder, Long> {
    Flux<StockOrder> findStockOrdersByUserId(String userId);
}
