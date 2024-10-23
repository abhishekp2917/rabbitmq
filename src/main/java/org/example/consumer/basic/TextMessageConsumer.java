package org.example.consumer.basic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.message.text", ackMode = "NONE")
    public void listenTextMessage(String message) {
        System.out.println(String.format("Queue : q.message.text, Message : %s", message));
    }

    public String consumeTextMessage(String queueName) {
        Object message = rabbitTemplate.receiveAndConvert(queueName);
        if(message!=null) return message.toString();
        else return null;
    }
}
