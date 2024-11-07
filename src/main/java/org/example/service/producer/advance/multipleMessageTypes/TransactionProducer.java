package org.example.service.producer.advance.multipleMessageTypes;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service class for producing and publishing transactions to RabbitMQ.
 *
 * This class is responsible for sending a batch of transactions to RabbitMQ with custom message properties,
 * including headers, delivery mode, priority, and content type.
 *
 * It utilizes RabbitTemplate to send messages to the specified exchange with the given routing key.
 */
@Service
public class TransactionProducer {

    // Autowired RabbitTemplate to send messages to RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a list of transactions to the specified RabbitMQ exchange and routing key.
     *
     * @param exchangeName The name of the exchange to which the message should be sent.
     * @param routingKey The routing key for the message.
     * @param transactions A list of transaction objects to be sent as messages.
     */
    public void publishTransaction(String exchangeName, String routingKey, List<Object> transactions) {
        // Define a custom message post-processor to manipulate message properties before sending
        MessagePostProcessor messagePostProcessor = message -> {
            // Set custom headers for the message (e.g., user identification)
            message.getMessageProperties().setHeader("user", "Abhishek");
            // Ensure the message is persistent (survives server restart)
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // Set message priority (used for message prioritization in RabbitMQ)
            message.getMessageProperties().setPriority(1);
            // Set the message content encoding to UTF-8
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            // Set content type as JSON (indicating that the payload is JSON formatted)
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };

        // Loop through the list of transactions and send each transaction as a message
        for (Object transaction : transactions) {
            // Create a correlation ID for each message (unique identifier for tracking the message)
            CorrelationData correlationData = new CorrelationData("MessageId-" + System.currentTimeMillis());

            // Send the message to RabbitMQ with the provided exchange name, routing key, and the message post-processor
            rabbitTemplate.convertAndSend(exchangeName, routingKey, transaction, messagePostProcessor, correlationData);
        }
    }
}
