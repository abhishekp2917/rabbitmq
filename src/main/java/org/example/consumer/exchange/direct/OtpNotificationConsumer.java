package org.example.consumer.exchange.direct;

import org.example.model.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpNotificationConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.notification.otp", ackMode = "NONE", concurrency = "3-5")
    public void listenOtpNotification(Notification notification) {
        System.out.println(String.format("Queue : q.notification.otp, Notification : %s", notification));
    }

    public Notification consumeOtpNotification() {
        try {
            Notification notification = (Notification)rabbitTemplate.receiveAndConvert("q.notification.otp");
            return notification;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
