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

    public void publishVideos(List<Video> videos) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        videos.forEach(employee -> rabbitTemplate.convertAndSend(Exchange.X_STREAM_VIDEO, "", employee, messagePostProcessor));
    }
}
