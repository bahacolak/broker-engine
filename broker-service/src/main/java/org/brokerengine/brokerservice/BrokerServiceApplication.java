package org.brokerengine.brokerservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class BrokerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokerServiceApplication.class, args);
    }

}
