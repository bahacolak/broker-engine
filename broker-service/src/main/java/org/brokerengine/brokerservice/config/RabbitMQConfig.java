package org.brokerengine.brokerservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${order.queue.name}")
    private String orderQName;

    @Value("${result.queue.name}")
    private String resultQName;

    private final AmqpAdmin amqpAdmin;

    public RabbitMQConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public Queue orderQueue() {
        Queue queue = new Queue(orderQName, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Queue resultQueue() {
        Queue queue = new Queue(resultQName, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }
}
