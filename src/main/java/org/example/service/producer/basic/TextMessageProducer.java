package org.example.service.producer.basic;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

/**
 * Service class for publishing plain text messages to a RabbitMQ queue.
 *
 * This class sends a simple text message to a specified RabbitMQ queue with custom properties like priority, delivery mode, and content encoding.
 */
@Service
public class TextMessageProducer {

    // Autowired RabbitTemplate to interact with RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a plain text message to a specified RabbitMQ queue.
     * It sets custom message properties like header, delivery mode, priority, content encoding, and content type.
     *
     * @param queueName The name of the RabbitMQ queue to send the message to.
     * @param textMessage The plain text message to send to the queue.
     */
    public void publishMessage(String queueName, String textMessage) {
        // MessagePostProcessor allows modification of message properties before sending
        MessagePostProcessor messagePostProcessor = message -> {
            // Setting a custom header to identify the user sending the message
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Setting delivery mode to NON_PERSISTENT (message will not survive broker crashes)
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);

            // Setting message priority to 1 (default priority)
            message.getMessageProperties().setPriority(1);

            // Setting content encoding to UTF-8 for proper handling of special characters
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Specifying the content type as plain text
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);

            return message;
        };

        // Send the plain text message to the specified queue using RabbitTemplate
        rabbitTemplate.convertAndSend(queueName, (Object)textMessage, messagePostProcessor);
    }
}
