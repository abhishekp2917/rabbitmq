package org.example.service.consumer.advance.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constants.Queue;
import org.example.model.Media;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This service listens for media upload messages from Google Cloud Platform (GCP).
 * It checks the `Q_MEDIA_UPLOAD_GCP` queue periodically and processes the media messages.
 */
@Service
public class MediaUploadGCPConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate for interacting with RabbitMQ

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for deserializing the message into a Media object

    /**
     * This method listens to the `Q_MEDIA_UPLOAD_GCP` queue for messages.
     * The listener runs periodically with a fixed delay of 5 seconds between executions.
     * The media message is deserialized and logged to the console if available.
     */
    @Scheduled(fixedDelay = 5000) // Runs every 5 seconds with a fixed delay after the last execution
    public void listenMediaUploadToGCP() {
        try {
            // Receive and convert the message from the `Q_MEDIA_UPLOAD_GCP` queue to a Media object
            Media media = objectMapper.convertValue(rabbitTemplate.receiveAndConvert(Queue.Q_MEDIA_UPLOAD_GCP), Media.class);

            // If the media object is not null, print the media details
            if (media != null) {
                System.out.println(String.format("Queue : %s, Media : %s", Queue.Q_MEDIA_UPLOAD_GCP, media));
            }
        } catch (Exception ex) {
            // Handle any exceptions that occur during message processing and log the error
            System.out.println(String.format("Error occurred : %s", ex.getMessage()));
        }
    }
}
