# Broker Engine

## Eureka Server
- Java 17, Netlix Eureka
- All services (except stock-service) are registered to eureka-server. This lets us avoid hardcoded service urls both in api-gateway and in user-service when it needs to call broker-service with Feign Client.

## Api Gateway
- Java 17, Spring Cloud Gateway
- Routes the API calls to the related service.

## User Service
- Java 17, Spring Boot REST, MySQL, Spring Cloud OpenFeign
- Registers a user. After the registration, user can log in.
- Hashes the raw password string with a unique salt for each new user.
- When a new user registers; user-service will call broker-service so that in broker-service, a new wallet for that particular user can be created.

## Broker Service
- Java 17, Spring Boot Reactive, MySQL, R2DBC, RabbitMQ
- After registration of a new user, user-service calls broker-service and a new wallet is created here.
- Before sending any orders, user must deposit some money.

### Buy Order
- User can send a buy order (as an event to a message broker) by defining the number of shares he/she wants to buy. 
- After sending the buy order to the message broker, he/she will not immediately have the stocks. The number of stocks that are expected to be bought will be added to "stockCountOnHold".
- Also the amount of whole order will be subtracted from "balance" and will be added to "balanceOnHold".
- Then the message broker will send an event which informs about whether the order was succesful or not. 
If succesful, the amount of the order will be subtracted from "balanceOnHold". 
Also the number of shares of the order will be subtracted from "stockCountOnHold" and added to "stockCount".
- "onHold" exists so that user will not be able to send orders with the funds/stocks of previous orders before those orders have been completed.

### Sell Order
- User can send a sell order (as an event to a message broker) by defining the number of shares he/she wants to sell. 
- After sending the sell order to the message broker, he/she will not immediately sell the stocks. The number of stocks that are expected to be sold will be added to "stockCountOnHold" and subtracted from "stockCount".
- Also the money that is expected to be gained from the sell order will be added to "balanceOnHold".
- Then the message broker will send an event which informs about whether the order was succesful or not. 
If succesful, the amount of the order will be subtracted from "balanceOnHold" and added to "balance". 
Also the number of shares of the order will be subtracted from "stockCountOnHold".

### Cancel Order
- User can send a cancel order in order to cancel a particular order.
- After sending the cancel order, broker-service changes that order's status to "canceled".
- Also it will undo every action that has happened in "wallet". (stockCount, stockCountOnHold, balance, balanceOnHold)

## Stock Service
- Node.js, RabbitMQ
- Listens to the events coming from broker-service.
- Sends result events after the order events (buy/sell) are completed (success or fail).
- Will not process a buy event if the requested number of shares are bigger than the remaining number of stocks in the stock-service.
- If a cancel event is sent from broker-service for a particular order, stock-service will stop processing that event and will not send a reply event for that order.
