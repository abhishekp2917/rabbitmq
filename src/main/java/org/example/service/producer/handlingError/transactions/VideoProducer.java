package org.example.service.producer.handlingError.transactions;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.example.model.Video;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a list of videos to the specified RabbitMQ exchange.
     *
     * @param exchangeName The name of the RabbitMQ exchange.
     * @param routingKey The routing key for message routing.
     * @param videos The list of videos to be published.
     * @return true if all videos are successfully published, false otherwise.
     */
    public boolean publishVideos(String exchangeName, String routingKey, List<Video> videos) {
        // Retrieve the RabbitMQ connection factory
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();

        // Ensure the connection factory is of type CachingConnectionFactory
        if (connectionFactory instanceof CachingConnectionFactory) {
            try (Connection connection = ((CachingConnectionFactory) connectionFactory).createConnection()) {
                Channel channel = connection.createChannel(true); // Create a transactional channel
                channel.txSelect(); // Start a transaction
                try {
                    Map<String, Object> messageHeaders = new HashMap<>();
                    messageHeaders.put("user", "Abhishek");

                    // Configure basic message properties
                    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                            .headers(messageHeaders)
                            .deliveryMode(MessageDeliveryMode.toInt(MessageDeliveryMode.PERSISTENT))
                            .priority(1)
                            .contentEncoding(StandardCharsets.UTF_8.toString())
                            .contentType(MessageProperties.CONTENT_TYPE_JSON)
                            .build();

                    // Prepare message properties
                    MessageProperties messageProperties = new MessageProperties();
                    messageProperties.setHeaders(messageHeaders);

                    // Iterate over the list of videos
                    for (Video video : videos) {
                        if (video.getVideoSizeInMB() <= 500) { // Only process videos <= 500 MB
                            Message message = rabbitTemplate.getMessageConverter().toMessage(video, messageProperties);
                            try {
                                // Publish message to the RabbitMQ exchange with routing key
                                channel.basicPublish(exchangeName, routingKey, basicProperties, message.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException("Error publishing message to exchange", e);
                            }
                        } else {
                            throw new UnsupportedOperationException("Video size is too long");
                        }
                    }
                    // Commit the transaction after successfully publishing all videos
                    channel.txCommit();
                    System.out.println("All the videos published successfully");
                } catch (Exception ex) {
                    // Rollback the transaction in case of an error
                    channel.txRollback();
                    System.out.println("Failed to publish the videos. Transaction rolled back.");
                    return false;
                }
            } catch (IOException ex) {
                System.out.println("Connection couldn't be established");
                return false;
            }
            return true;
        } else {
            // Return false if the connection factory is not of type CachingConnectionFactory
            return false;
        }
    }
}
