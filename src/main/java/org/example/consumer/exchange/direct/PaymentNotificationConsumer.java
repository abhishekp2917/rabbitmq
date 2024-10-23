package org.example.consumer.exchange.direct;

import org.example.model.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.notification.payment", ackMode = "NONE", concurrency = "3-5")
    public void listenPaymentNotification(Notification notification) {
        System.out.println(String.format("Queue : q.notification.payment, Notification : %s", notification));
    }

    public Notification consumePaymentNotification() {
        try {
            Notification notification = (Notification)rabbitTemplate.receiveAndConvert("q.notification.payment");
            return notification;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
