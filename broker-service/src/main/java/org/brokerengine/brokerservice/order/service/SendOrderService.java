package org.brokerengine.brokerservice.order.service;

import org.brokerengine.brokerservice.order.entity.CancelOrder;
import org.brokerengine.brokerservice.order.entity.StockOrder;
import org.brokerengine.brokerservice.order.request.OrderRequest;
import reactor.core.publisher.Mono;

public interface SendOrderService {
    Mono<StockOrder> sendBuyOrder(OrderRequest request);
    Mono<StockOrder> sendSellOrder(OrderRequest request);
    Mono<CancelOrder> sendCancelOrder(String orderId);
}
