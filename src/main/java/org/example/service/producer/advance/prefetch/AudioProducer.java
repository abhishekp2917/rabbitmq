package org.example.service.producer.advance.prefetch;

import org.example.constants.Exchange;
import org.example.model.Audio;
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
 * Service class responsible for producing and publishing Audio objects to RabbitMQ.
 *
 * This class uses the RabbitTemplate to send a list of Audio objects to a specified RabbitMQ exchange with
 * custom message properties such as persistence, priority, content encoding, and content type.
 */
@Service
public class AudioProducer {

    // Autowire RabbitTemplate to send messages to RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a list of Audio objects to the specified RabbitMQ exchange.
     *
     * @param audios A list of Audio objects to be sent as messages.
     */
    public void publishAudios(List<Audio> audios) {
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

        // Loop through the list of Audio objects and send each one as a message to RabbitMQ
        for (Audio audio : audios) {
            // Generate a unique correlation ID for each message, based on the current timestamp
            CorrelationData correlationData = new CorrelationData("MessageId-" + System.currentTimeMillis());

            // Send the Audio object to RabbitMQ with the exchange name, routing key (empty in this case),
            // the audio object as the message body, the message post-processor, and the correlation data
            rabbitTemplate.convertAndSend(Exchange.X_AUDIO, "", audio, messagePostProcessor, correlationData);
        }
    }
}
