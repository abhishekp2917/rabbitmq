package org.example.service.producer.handlingError.deadLetterExchange;

import org.example.model.Encoding;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class EncodingProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes an Encoding message to the specified exchange with a routing key.
     *
     * @param exchangeName The name of the RabbitMQ exchange.
     * @param routingKey The routing key used for message routing.
     * @param encoding The Encoding object to be sent.
     */
    public void publishEncoding(String exchangeName, String routingKey, Encoding encoding) {
        // Configure message properties using MessagePostProcessor
        MessagePostProcessor messagePostProcessor = message -> {
            // Add custom headers to the message
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set message delivery mode to PERSISTENT for durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set message priority
            message.getMessageProperties().setPriority(1);

            // Set message content encoding and content type for correct interpretation
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            return message;
        };

        // Send the encoding object to the exchange using the specified routing key
        rabbitTemplate.convertAndSend(exchangeName, routingKey, encoding, messagePostProcessor);
    }
}
