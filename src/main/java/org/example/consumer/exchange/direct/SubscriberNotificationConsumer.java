package org.example.consumer.exchange.direct;

import org.example.model.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.notification.subscriber", ackMode = "NONE", concurrency = "3-5")
    public void listenSubscriberNotification(Notification notification) {
        System.out.println(String.format("Queue : q.notification.subscriber, Notification : %s", notification));
    }

    public Notification consumeSubscriberNotification() {
        try {
            Notification notification = (Notification)rabbitTemplate.receiveAndConvert("q.notification.subscriber");
            return notification;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
