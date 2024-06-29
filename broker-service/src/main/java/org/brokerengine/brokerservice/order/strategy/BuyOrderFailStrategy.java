package org.brokerengine.brokerservice.order.strategy;

import org.brokerengine.brokerservice.order.entity.StockOrder;
import org.brokerengine.brokerservice.order.enums.OrderStatus;
import org.brokerengine.brokerservice.order.repository.OrderRepository;
import org.brokerengine.brokerservice.wallet.repository.WalletRepository;

public class BuyOrderFailStrategy implements OrderStrategy {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;

    public BuyOrderFailStrategy(OrderRepository orderRepository, WalletRepository walletRepository) {
        this.orderRepository = orderRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public void adjustWallet(String orderId) {
        StockOrder stockOrder = new StockOrder();
        orderRepository.findById(Long.valueOf(orderId))
                .flatMap(o -> {
                    o.setOrderStatus(OrderStatus.FAILED.label);
                    return orderRepository.save(o);
                })
                .flatMap(o -> {
                    stockOrder.setNumberOfShares(o.getNumberOfShares());
                    stockOrder.setPrice(o.getPrice());
                    return walletRepository.findByUserId(o.getUserId());
                })
                .flatMap(w -> {
                    Double orderAmount = stockOrder.getPrice() * stockOrder.getNumberOfShares();
                    w.setStockCountOnHold(w.getStockCountOnHold() - stockOrder.getNumberOfShares());
                    w.setBalanceOnHold(w.getBalanceOnHold() - orderAmount);
                    w.setBalance(w.getBalance() + orderAmount);
                    return walletRepository.save(w);
                })
                .block();
    }
}
