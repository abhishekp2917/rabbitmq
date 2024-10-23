package org.example.consumer.exchange.fanout;

import org.example.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.order.notification", ackMode = "NONE", concurrency = "3-5")
    public void listenOrderNotification(Order order) {
        System.out.println(String.format("Queue : q.order.notification, Order : %s", order));
    }

    public Order consumeOrderNotification() {
        try {
            Order order = (Order)rabbitTemplate.receiveAndConvert("q.order.notification");
            return order;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
