package org.example.service.consumer.exchange.topic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Notification;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpNotificationConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_NOTIFICATION_OTP,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                                value = org.example.constants.Queue.Q_NOTIFICATION_OTP,
                                durable = "true",
                                exclusive = "false",
                                autoDelete = "false",
                                arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                                }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_NOTIFICATION, type = ExchangeType.TOPIC, durable = "true", autoDelete = "false"),
                    key = "#.otp.#"
            )
    )
    public void listenOtpNotification(Notification notification) {
        System.out.println(String.format("Queue : %s, Notification : %s", org.example.constants.Queue.Q_NOTIFICATION_OTP, notification));
    }

    public Notification consumeOtpNotification() {
        try {
            Notification notification = (Notification)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_NOTIFICATION_OTP);
            return notification;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
