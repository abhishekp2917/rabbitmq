package org.example.service.producer.stream.basic;

import org.example.constants.Stream;
import org.example.model.Employee;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JsonMessageStreamProducer {

    @Autowired
    @Qualifier(Stream.S_MESSAGE_JSON)  // Injecting RabbitStreamTemplate using a qualifier to select the appropriate stream
    private RabbitStreamTemplate rabbitStreamTemplate;

    /**
     * Publishes a list of Employee objects as JSON messages to a RabbitMQ stream.
     *
     * @param employees The list of employees to be published as messages.
     */
    public void publishJsonMessage(List<Employee> employees) {
        // Define the MessagePostProcessor to set custom message properties
        MessagePostProcessor messagePostProcessor = message -> {
            // Set custom header with the key "user" and value "Abhishek"
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set the message delivery mode to PERSISTENT to ensure message durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set the message priority to 1 (lowest priority)
            message.getMessageProperties().setPriority(1);

            // Set the content encoding to UTF-8
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Set the content type to JSON
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Return the modified message
            return message;
        };

        // Loop through each employee and publish them to the stream
        employees.forEach(employee ->
                // Convert and send each employee as a message to the stream using the provided messagePostProcessor
                rabbitStreamTemplate.convertAndSend(employee, messagePostProcessor)
        );
    }
}
