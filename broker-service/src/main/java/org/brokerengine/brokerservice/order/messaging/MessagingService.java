package org.brokerengine.brokerservice.order.messaging;

public interface MessagingService {
    void sendMessage(String message);
    void receiveMessage(String message);
}
