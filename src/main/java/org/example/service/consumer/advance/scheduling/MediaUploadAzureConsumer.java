package org.example.service.consumer.advance.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Media;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This service listens for media upload messages to Azure. It uses scheduled tasks to start and stop the listener
 * at specific times, and it consumes media messages from the queue.
 */
@Service
public class MediaUploadAzureConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate for interacting with the RabbitMQ broker

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for deserializing the received message into a Media object

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry; // Used to start/stop the listener container

    /**
     * This method starts the MediaUploadAzureConsumer listener at the scheduled time (13:05:00 every day).
     * It triggers the listener container to begin consuming messages from the `Q_MEDIA_UPLOAD_AZURE` queue.
     */
    @Scheduled(cron = "0 5 13 * * *") // Cron expression: at 13:05:00 every day
    public void startMediaUploadToAzureConsumer() {
        // Start the listener container for the queue `Q_MEDIA_UPLOAD_AZURE`
        rabbitListenerEndpointRegistry.getListenerContainer(org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE).start();
    }

    /**
     * This method stops the MediaUploadAzureConsumer listener at the scheduled time (13:03:45 every day).
     * It triggers the listener container to stop consuming messages from the `Q_MEDIA_UPLOAD_AZURE` queue.
     */
    @Scheduled(cron = "45 3 13 * * *") // Cron expression: at 13:03:45 every day
    public void stopMediaUploadToAzureConsumer() {
        // Stop the listener container for the queue `Q_MEDIA_UPLOAD_AZURE`
        rabbitListenerEndpointRegistry.getListenerContainer(org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE).stop();
    }

    /**
     * This method listens for incoming messages from the `Q_MEDIA_UPLOAD_AZURE` queue.
     * The media message is deserialized and logged to the console.
     * The method is triggered when a message with the routing key matching `#.azure.#` is published to the queue.
     *
     * @param media The deserialized Media object from the received message
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE, // Listener ID is tied to the `Q_MEDIA_UPLOAD_AZURE` queue
            ackMode = ConsumerAcknowledgementMode.AUTO, // Auto-acknowledge the message after it is processed
            concurrency = "3-5", // The consumer will use 3 to 5 concurrent threads to process the messages
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE, // The queue to listen to
                            durable = "true", // The queue is durable, meaning it will survive RabbitMQ restarts
                            exclusive = "false", // The queue is not exclusive (it can be shared by other consumers)
                            autoDelete = "false", // The queue will not be automatically deleted when there are no consumers
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Queue type set to "CLASSIC"
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_MEDIA, type = ExchangeType.TOPIC, durable = "true", autoDelete = "false"), // Binding to the exchange with type "TOPIC"
                    key = "#.azure.#" // The routing key pattern for messages that this listener should receive
            )
    )
    public void listenMediaUploadToAzure(Media media) {
        // Log the media object that was received from the queue `Q_MEDIA_UPLOAD_AZURE`
        System.out.println(String.format("Queue : %s, Media : %s", org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE, media));
    }
}
