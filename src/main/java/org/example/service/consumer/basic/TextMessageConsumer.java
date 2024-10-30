package org.example.service.consumer.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = Queue.Q_MESSAGE_TEXT,
            queues = Queue.Q_MESSAGE_TEXT,
            ackMode = ConsumerAcknowledgementMode.NONE)
    public void listenTextMessage(String message) {
        System.out.println(String.format("Queue : %s, Message : %s", Queue.Q_MESSAGE_TEXT, message));
    }

    public String consumeTextMessage(String queueName) {
        Object message = rabbitTemplate.receiveAndConvert(queueName);
        if(message!=null) return message.toString();
        else return null;
    }
}
