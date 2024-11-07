package org.example.service.producer.advance.scheduling;

import org.example.model.Media;
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
 * Service class responsible for producing and publishing Media objects to RabbitMQ.
 *
 * This class uses the RabbitTemplate to send a list of Media objects to a specified RabbitMQ exchange with
 * custom message properties such as persistence, priority, content encoding, and content type.
 */
@Service
public class MediaProducer {

    // Autowired RabbitTemplate to send messages to RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a list of Media objects to the specified RabbitMQ exchange.
     *
     * @param exchangeName The name of the RabbitMQ exchange to which the messages will be sent.
     * @param routingKey The routing key used to route the message to the correct queue.
     * @param medias A list of Media objects to be sent as messages.
     */
    public void publishMedias(String exchangeName, String routingKey, List<Media> medias) {
        // Define a custom message post-processor to modify the message properties before sending
        MessagePostProcessor messagePostProcessor = message -> {
            // Set a custom header with the user information
            message.getMessageProperties().setHeader("user", "Abhishek");
            // Ensure the message is persistent, meaning it will survive a broker restart
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // Set message priority to low (1 is the lowest priority)
            message.getMessageProperties().setPriority(1);
            // Set the content encoding to UTF-8 for proper character encoding handling
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            // Indicate that the message body is in JSON format
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };

        // Loop through the list of Media objects and send each one as a message to RabbitMQ
        for (Media media : medias) {
            // Generate a unique correlation ID for each message, based on the current timestamp
            CorrelationData correlationData = new CorrelationData("MessageId-" + System.currentTimeMillis());

            // Send the Media object to RabbitMQ with the exchange name, routing key,
            // the media object as the message body, the message post-processor, and the correlation data
            rabbitTemplate.convertAndSend(exchangeName, routingKey, media, messagePostProcessor, correlationData);
        }
    }
}
