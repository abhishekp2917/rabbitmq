package org.example.service.producer.stream.offset;

import org.example.constants.Exchange;
import org.example.model.Video;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class VideoStreamProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a list of video objects to a RabbitMQ stream with custom message properties.
     *
     * @param videos The list of video objects to be published.
     */
    public void publishVideos(List<Video> videos) {
        // Define the MessagePostProcessor to customize message properties
        MessagePostProcessor messagePostProcessor = message -> {
            // Set the custom header with the key "user" and value "Abhishek"
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set the message delivery mode to PERSISTENT to ensure message durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set the message priority to 1 (lowest priority)
            message.getMessageProperties().setPriority(1);

            // Set the content encoding to UTF-8
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Set the content type to JSON
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Return the modified message with set properties
            return message;
        };

        // Loop through each video object and publish them to the stream
        videos.forEach(video ->
                // Convert and send each video object to the specified stream exchange
                rabbitTemplate.convertAndSend(Exchange.X_STREAM_VIDEO, "", video, messagePostProcessor)
        );
    }
}
