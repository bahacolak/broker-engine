package org.brokerengine.brokerservice.order.repository;

import org.brokerengine.brokerservice.order.entity.CancelOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CancelOrderRepository extends ReactiveCrudRepository<CancelOrder, Long> {
}
