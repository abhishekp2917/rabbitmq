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

@Service
public class MediaProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishMedias(String exchangeName, String routingKey, List<Media> medias) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        for(Media media : medias) {
            CorrelationData correlationData = new CorrelationData("MessageId-" + System.currentTimeMillis());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, media, messagePostProcessor, correlationData);
        }
    }
}
