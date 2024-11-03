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

    public boolean publishVideos(String exchangeName, String routingKey, List<Video> videos) {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        if(connectionFactory instanceof CachingConnectionFactory) {
            try(Connection connection = ((CachingConnectionFactory)connectionFactory).createConnection()) {
                Channel channel = connection.createChannel(true);
                channel.txSelect();
                try {
                    Map<String, Object> messageHeaders = new HashMap<>();
                    messageHeaders.put("user", "Abhishek");
                    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                            .headers(messageHeaders)
                            .deliveryMode(MessageDeliveryMode.toInt(MessageDeliveryMode.PERSISTENT))
                            .priority(1)
                            .contentEncoding(StandardCharsets.UTF_8.toString())
                            .contentType(MessageProperties.CONTENT_TYPE_JSON)
                            .build();
                    MessageProperties messageProperties = new MessageProperties();
                    messageProperties.setHeaders(messageHeaders);
                    videos.forEach(video -> {
                        if(video.getVideoSizeInMB()<=500) {
                            Message message = rabbitTemplate.getMessageConverter().toMessage(video, messageProperties);
                            try {
                                channel.basicPublish(exchangeName, routingKey, basicProperties, message.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else throw new UnsupportedOperationException("Video size is too long");
                    });
                    channel.txCommit();
                    System.out.println("All the videos published successfully");
                }
                catch (Exception ex) {
                    channel.txRollback();
                    System.out.println("Failed to publish the videos. Transaction roll backed successfully");
                    return false;
                }
            }
            catch (IOException ex) {
                System.out.println("Connection couldn't be established");
                return false;
            }
            return true;
        }
        else return false;
    }
}
