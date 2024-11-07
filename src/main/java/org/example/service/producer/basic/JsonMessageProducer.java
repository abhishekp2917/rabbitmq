package org.example.service.producer.basic;

import org.example.model.Employee;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

/**
 * Service class for producing and publishing JSON messages to a specified RabbitMQ queue.
 *
 * This class uses the RabbitTemplate to send an Employee object as a JSON message to the RabbitMQ queue.
 * It also customizes the message properties such as delivery mode, priority, encoding, and content type.
 */
@Service
public class JsonMessageProducer {

    // Autowired RabbitTemplate for sending messages to RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes an Employee object as a JSON message to the specified RabbitMQ queue.
     *
     * @param queueName The name of the RabbitMQ queue to which the message will be sent.
     * @param employee The Employee object to be sent as the message body.
     */
    public void publishMessage(String queueName, Employee employee) {
        // Create a message post-processor to customize message properties
        MessagePostProcessor messagePostProcessor = message -> {
            // Set a custom header to the message, can be used for logging or tracing
            message.getMessageProperties().setHeader("user", "Abhishek");
            // Make the message persistent, ensuring it is not lost in case of a RabbitMQ crash
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // Set message priority to low (1 is the lowest priority)
            message.getMessageProperties().setPriority(1);
            // Set content encoding to UTF-8 for proper handling of special characters
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            // Indicate that the message content type is JSON
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };

        // Send the Employee object as the message body to the specified queue with the custom message properties
        rabbitTemplate.convertAndSend(queueName, employee, messagePostProcessor);
    }
}
