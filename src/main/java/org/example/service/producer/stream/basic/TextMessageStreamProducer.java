package org.example.service.producer.stream.basic;

import org.example.constants.Stream;
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
public class TextMessageStreamProducer {

    @Autowired
    @Qualifier(Stream.S_MESSAGE_TEXT)  // Injecting RabbitStreamTemplate using a qualifier to select the appropriate stream
    private RabbitStreamTemplate rabbitStreamTemplate;

    /**
     * Publishes a list of text messages to a RabbitMQ stream.
     *
     * @param textMessages The list of text messages to be published to the stream.
     */
    public void publishTextMessage(List<String> textMessages) {
        // Define the MessagePostProcessor to set custom message properties for each message
        MessagePostProcessor messagePostProcessor = message -> {
            // Set custom header with the key "user" and value "Abhishek"
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set the message delivery mode to PERSISTENT to ensure message durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set the message priority to 1 (lowest priority)
            message.getMessageProperties().setPriority(1);

            // Set the content encoding to UTF-8
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Set the content type to plain text
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);

            // Return the modified message
            return message;
        };

        // Loop through each text message and publish them to the stream
        textMessages.forEach(textMessage ->
                // Convert and send each text message to the stream using the provided messagePostProcessor
                rabbitStreamTemplate.convertAndSend(textMessage, messagePostProcessor)
        );
    }
}
