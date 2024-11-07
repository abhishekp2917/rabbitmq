package org.example.service.consumer.advance.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constants.Queue;
import org.example.model.Media;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This service listens for media upload messages from the AWS queue at regular intervals.
 * It uses Spring's @Scheduled annotation to periodically check the queue and process any pending messages.
 * If a message is found, it is deserialized into a `Media` object, and further processing can occur.
 */
@Service
public class MediaUploadAWSConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate used to interact with the RabbitMQ broker

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper used to deserialize the message into a Media object

    /**
     * This method listens for media upload messages to AWS in the queue `Q_MEDIA_UPLOAD_AWS`.
     * The method is executed at a fixed rate, with an initial delay before the first execution.
     * If a message is found, it is converted to a `Media` object and logged.
     * The error handling ensures any exception is caught and logged without crashing the application.
     */
    @Scheduled(fixedRate = 5000, initialDelay = 10000)
    public void listenMediaUploadToAWS() {
        try {
            // Retrieve and convert the message from the queue to a Media object
            Media media = objectMapper.convertValue(rabbitTemplate.receiveAndConvert(Queue.Q_MEDIA_UPLOAD_AWS), Media.class);

            // If the message is not null, log it
            if(media != null) {
                System.out.println(String.format("Queue : %s, Media : %s", Queue.Q_MEDIA_UPLOAD_AWS, media));
            }
        }
        catch (Exception ex) {
            // If an error occurs, catch the exception and log the error message
            System.out.println(String.format("Error occurred : %s", ex.getMessage()));
        }
    }
}
