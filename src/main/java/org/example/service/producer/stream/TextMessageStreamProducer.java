package org.example.service.producer.stream;

import org.example.constants.Stream;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class TextMessageStreamProducer {

    @Autowired
    @Qualifier(Stream.S_MESSAGE_TEXT)
    private RabbitStreamTemplate rabbitStreamTemplate;

    public void publishTextMessage(String textMessage) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            return message;
        };
        rabbitStreamTemplate.convertAndSend(textMessage, messagePostProcessor);
    }
}
