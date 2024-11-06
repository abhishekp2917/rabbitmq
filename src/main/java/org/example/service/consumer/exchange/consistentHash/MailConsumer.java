package org.example.service.consumer.exchange.consistentHash;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Mail;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_MAIL_1,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_MAIL_1,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_MAIL, type = ExchangeType.X_CONSISTENT_HASH, durable = "true", autoDelete = "false"),
                    key = {"concert.music", "match.ipl"}
            )
    )
    public void listenMailQueue1(Mail mail) {
        System.out.println(String.format("Queue : %s, Mail : %s", org.example.constants.Queue.Q_MAIL_1, mail));
    }

    @RabbitListener(
            id = org.example.constants.Queue.Q_MAIL_2,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_MAIL_2,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_MAIL, type = ExchangeType.X_CONSISTENT_HASH, durable = "true", autoDelete = "false"),
                    key = {"concert.music", "match.ipl"}
            )
    )
    public void listenMailQueue2(Mail mail) {
        System.out.println(String.format("Queue : %s, Mail : %s", org.example.constants.Queue.Q_MAIL_2, mail));
    }

}
