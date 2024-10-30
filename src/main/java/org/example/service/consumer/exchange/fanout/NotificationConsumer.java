package org.example.service.consumer.exchange.fanout;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_ORDER_NOTIFICATION,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ORDER_NOTIFICATION,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_ORDER, type = ExchangeType.FANOUT, durable = "true", autoDelete = "false")
            )
    )
    public void listenOrderNotification(Order order) {
        System.out.println(String.format("Queue : %s, Order : %s", org.example.constants.Queue.Q_ORDER_NOTIFICATION, order));
    }

    public Order consumeOrderNotification() {
        try {
            Order order = (Order)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ORDER_NOTIFICATION);
            return order;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
