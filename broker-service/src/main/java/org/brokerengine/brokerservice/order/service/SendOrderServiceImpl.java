package org.brokerengine.brokerservice.order.service;

import com.google.gson.Gson;
import org.brokerengine.brokerservice.order.entity.CancelOrder;
import org.brokerengine.brokerservice.order.entity.StockOrder;
import org.brokerengine.brokerservice.order.enums.OrderStatus;
import org.brokerengine.brokerservice.order.enums.OrderType;
import org.brokerengine.brokerservice.order.exception.CannotCancelOrderException;
import org.brokerengine.brokerservice.order.exception.InsufficientFundsException;
import org.brokerengine.brokerservice.order.exception.InsufficientStocksException;
import org.brokerengine.brokerservice.order.exception.OrderNotFoundException;
import org.brokerengine.brokerservice.order.messaging.MessageSendDto;
import org.brokerengine.brokerservice.order.messaging.MessagingService;
import org.brokerengine.brokerservice.order.repository.CancelOrderRepository;
import org.brokerengine.brokerservice.order.repository.OrderRepository;
import org.brokerengine.brokerservice.order.request.OrderRequest;
import org.brokerengine.brokerservice.wallet.exception.WalletNotFoundException;
import org.brokerengine.brokerservice.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class SendOrderServiceImpl implements SendOrderService {
    private final OrderRepository orderRepository;
    private final CancelOrderRepository cancelOrderRepository;
    private final WalletRepository walletRepository;
    private final MessagingService messagingService;
    private final Gson gson = new Gson();

    public SendOrderServiceImpl(OrderRepository orderRepository,
                                CancelOrderRepository cancelOrderRepository,
                                WalletRepository walletRepository, MessagingService messagingService) {
        this.orderRepository = orderRepository;
        this.cancelOrderRepository = cancelOrderRepository;
        this.walletRepository = walletRepository;
        this.messagingService = messagingService;
    }

    @Override
    public Mono<StockOrder> sendBuyOrder(OrderRequest request) {
        Double orderAmount = request.getPrice() * request.getNumberOfShares();
        StockOrder buyOrder = new StockOrder(request.getUserId(), request.getNumberOfShares(), request.getPrice(), OrderStatus.PROCESSING.label, OrderType.BUY.label);

        return walletRepository.findByUserId(request.getUserId())
                .switchIfEmpty(Mono.error(new WalletNotFoundException()))
                .flatMap(w -> {
                    if (orderAmount > w.getBalance()) {
                        return Mono.error(new InsufficientFundsException());
                    } else {
                        w.setBalance(w.getBalance() - orderAmount);
                        w.setBalanceOnHold(w.getBalanceOnHold() + orderAmount);
                        w.setStockCountOnHold(w.getStockCountOnHold() + request.getNumberOfShares());
                        return walletRepository.save(w);
                    }
                })
                .flatMap(w -> orderRepository.save(buyOrder))
                .doOnNext(o -> {
                    MessageSendDto messageSendDto = new MessageSendDto(o.getId().toString(), OrderType.BUY.label, request.getNumberOfShares(), request.getPrice());
                    String json = gson.toJson(messageSendDto);
                    messagingService.sendMessage(json);
                });
    }

    @Override
    public Mono<StockOrder> sendSellOrder(OrderRequest request) {
        Double orderAmount = request.getPrice() * request.getNumberOfShares();
        StockOrder sellOrder = new StockOrder(request.getUserId(), request.getNumberOfShares(), request.getPrice(), OrderStatus.PROCESSING.label, OrderType.SELL.label);

        return walletRepository.findByUserId(request.getUserId())
                .switchIfEmpty(Mono.error(new WalletNotFoundException()))
                .flatMap(w -> {
                    if (w.getStockCount() < request.getNumberOfShares()) {
                        return Mono.error(new InsufficientStocksException());
                    } else {
                        w.setBalanceOnHold(w.getBalanceOnHold() + orderAmount);
                        w.setStockCount(w.getStockCount() - request.getNumberOfShares());
                        w.setStockCountOnHold(w.getStockCountOnHold() + request.getNumberOfShares());
                        return walletRepository.save(w);
                    }
                })
                .flatMap(w -> orderRepository.save(sellOrder))
                .doOnNext(o -> {
                    MessageSendDto messageSendDto = new MessageSendDto(o.getId().toString(), OrderType.SELL.label, request.getNumberOfShares(), request.getPrice());
                    String json = gson.toJson(messageSendDto);
                    messagingService.sendMessage(json);
                });
    }

    @Override
    public Mono<CancelOrder> sendCancelOrder(String orderId) {
        final StockOrder stockOrder = new StockOrder();
        return orderRepository.findById(Long.valueOf(orderId))
                .switchIfEmpty(Mono.error(new OrderNotFoundException()))
                .flatMap(o -> {
                    if (!Objects.equals(o.getOrderStatus(), OrderStatus.PROCESSING.label)) {
                        return Mono.error(new CannotCancelOrderException());
                    } else {
                        o.setOrderStatus(OrderStatus.CANCELED.label);
                        return orderRepository.save(o);
                    }
                })
                .flatMap(o -> {
                    stockOrder.setOrderType(o.getOrderType());
                    stockOrder.setNumberOfShares(o.getNumberOfShares());
                    stockOrder.setPrice(o.getPrice());
                    return walletRepository.findByUserId(o.getUserId());
                })
                .flatMap(w -> {
                    Double orderAmount = stockOrder.getPrice() * stockOrder.getNumberOfShares();
                    if (Objects.equals(stockOrder.getOrderType(), OrderType.BUY.label)) {
                        w.setStockCountOnHold(w.getStockCountOnHold() - stockOrder.getNumberOfShares());
                        w.setBalanceOnHold(w.getBalanceOnHold() - orderAmount);
                        w.setBalance(w.getBalance() + orderAmount);
                    } else if (Objects.equals(stockOrder.getOrderType(), OrderType.SELL.label)) {
                        w.setStockCountOnHold(w.getStockCountOnHold() - stockOrder.getNumberOfShares());
                        w.setBalanceOnHold(w.getBalanceOnHold() - orderAmount);
                        w.setStockCount(w.getStockCount() + stockOrder.getNumberOfShares());
                    }
                    return walletRepository.save(w);
                })
                .flatMap(w -> cancelOrderRepository.save(new CancelOrder(orderId)))
                .doOnNext(c -> {
                    MessageSendDto messageSendDto = MessageSendDto.builder().orderId(orderId).orderType(OrderType.CANCEL.label).build();
                    String json = gson.toJson(messageSendDto);
                    messagingService.sendMessage(json);
                });
    }
}
