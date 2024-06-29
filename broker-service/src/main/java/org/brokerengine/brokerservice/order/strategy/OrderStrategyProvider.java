package org.brokerengine.brokerservice.order.strategy;

import org.brokerengine.brokerservice.order.enums.OrderStatus;
import org.brokerengine.brokerservice.order.enums.OrderType;
import org.brokerengine.brokerservice.order.repository.OrderRepository;
import org.brokerengine.brokerservice.wallet.repository.WalletRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderStrategyProvider {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;

    public OrderStrategyProvider(OrderRepository orderRepository, WalletRepository walletRepository) {
        this.orderRepository = orderRepository;
        this.walletRepository = walletRepository;
    }

    public OrderStrategy provide(String orderType, String orderStatus) {
        if (Objects.equals(orderType, OrderType.BUY.label) && Objects.equals(orderStatus, OrderStatus.DONE.label)) {
            return new BuyOrderSuccessStrategy(orderRepository, walletRepository);
        } else if (Objects.equals(orderType, OrderType.BUY.label) && Objects.equals(orderStatus, OrderStatus.FAILED.label)) {
            return new BuyOrderFailStrategy(orderRepository, walletRepository);
        } else if (Objects.equals(orderType, OrderType.SELL.label) && Objects.equals(orderStatus, OrderStatus.DONE.label)) {
            return new SellOrderSuccessStrategy(orderRepository, walletRepository);
        } else {
            return new SellOrderFailStrategy(orderRepository, walletRepository);
        }
    }
}
