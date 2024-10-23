package org.example.producer.basic;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class TextMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishMessage(String queueName, String textMessage) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            return message;
        };
        rabbitTemplate.convertAndSend(queueName, (Object)textMessage, messagePostProcessor);
    }
}
