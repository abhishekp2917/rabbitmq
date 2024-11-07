package org.example.service.producer.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.constants.Queue;
import org.example.model.Video;
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
 * Service class responsible for publishing messages to a priority queue in RabbitMQ.
 *
 * The messages represent `Video` objects and the priority of the message is determined by the `Video` object's priority value.
 * This class uses RabbitTemplate for message conversion, sending, and configuring message properties such as delivery mode, priority, and headers.
 * Additionally, it configures the publisher confirms to log whether the message was acknowledged by the broker.
 */
@Service
public class PriorityQueueMessageProducer {

    // Autowired RabbitTemplate to interact with RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Autowired ObjectMapper to deserialize and serialize JSON objects
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Configures publisher confirms to log acknowledgment information when a message is sent to RabbitMQ.
     * This callback is called when a message is acknowledged (or not) by the RabbitMQ broker.
     *
     * This method runs after the bean has been initialized.
     */
    @PostConstruct
    public void configurePublisherConfirms() {
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                System.out.println("Message acknowledged by broker with ID: " + ((correlationData != null) ? correlationData.getId() : -1));
            } else {
                System.err.println("Message NOT acknowledged by broker with ID: " + ((correlationData != null) ? correlationData.getId() : -1) + ". Cause: " + cause);
            }
        });
    }

    /**
     * Publishes `Video` messages to a RabbitMQ priority queue with custom properties.
     * The message priority is set based on the `priority` field of the `Video` object.
     * The method serializes the `Video` object to JSON, sets the appropriate message headers, and ensures message persistence.
     *
     * @param videos A list of `Video` objects to be published to the RabbitMQ priority queue.
     */
    public void publishPriorityQueueMessages(List<Video> videos) {
        // MessagePostProcessor to modify the message properties before sending
        MessagePostProcessor messagePostProcessor = message -> {
            try {
                // Deserializing the message body to extract the video object to set its priority
                Video video = objectMapper.readValue(message.getBody(), Video.class);
                // Set the priority based on the Video object's priority
                message.getMessageProperties().setPriority(video.getPriority());
            } catch (Exception ex) {
                // Default to priority 1 if an error occurs while setting priority
                message.getMessageProperties().setPriority(1);
            }

            // Set other message properties for consistency and message handling
            message.getMessageProperties().setHeader("user", "Abhishek");  // Add custom header (e.g., user info)
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);  // Ensure message persistence
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());  // Set content encoding
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);  // Specify message type as JSON
            return message;
        };

        // Loop through each Video object and send it to the RabbitMQ priority queue
        videos.forEach(video -> {
            // Create a unique CorrelationData for tracking the message ID
            CorrelationData correlationData = new CorrelationData("MessageId-" + video.getVideoId());
            // Send the Video object to the specified priority queue
            rabbitTemplate.convertAndSend(Queue.Q_PRIORITY_MESSAGE, video, messagePostProcessor, correlationData);
        });
    }
}
