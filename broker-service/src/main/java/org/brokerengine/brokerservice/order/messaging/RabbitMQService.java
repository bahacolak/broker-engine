package org.brokerengine.brokerservice.order.messaging;

import com.google.gson.Gson;
import org.brokerengine.brokerservice.order.strategy.OrderStrategyProvider;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Queue;



@Service
public class RabbitMQService implements MessagingService {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private final OrderStrategyProvider orderStrategyProvider;
    private final Gson gson = new Gson();

    public RabbitMQService(RabbitTemplate rabbitTemplate, Queue queue, OrderStrategyProvider orderStrategyProvider) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
        this.orderStrategyProvider = orderStrategyProvider;
    }

    @Override
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(this.queue.getName(), message);
    }

    @RabbitListener(queues = {"${result.queue.name}"})
    @Override
    public void receiveMessage(@Payload String message) {
        System.out.println("Message " + message);
        MessageReceiveDto messageReceiveDto = gson.fromJson(message, MessageReceiveDto.class);

        orderStrategyProvider
                .provide(messageReceiveDto.getOrderType(), messageReceiveDto.getStatus())
                .adjustWallet(messageReceiveDto.getOrderId());

    }
}
