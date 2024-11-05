package org.example.service.consumer.stream.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Stream;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageStreamConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = Stream.S_MESSAGE_TEXT,
            queues = Stream.S_MESSAGE_TEXT,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenTextMessageStream(String message) {
        System.out.println(String.format("Stream : %s, Message : %s", Stream.S_MESSAGE_TEXT, message));
    }
}
