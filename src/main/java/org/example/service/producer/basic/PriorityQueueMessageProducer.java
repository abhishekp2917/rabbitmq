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

@Service
public class PriorityQueueMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void configurePublisherConfirms() {
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                System.out.println("Message acknowledged by broker with ID: " + ((correlationData!=null)?correlationData.getId():-1));
            }
            else {
                System.err.println("Message NOT acknowledged by broker with ID: " + ((correlationData!=null)?correlationData.getId():-1) + ". Cause: " + cause);
            }
        });
    }

    public void publishPriorityQueueMessages(List<Video> videos) {
        MessagePostProcessor messagePostProcessor = message -> {
            try {
                Video video = objectMapper.readValue(message.getBody(), Video.class);
                message.getMessageProperties().setPriority(video.getPriority());
            }
            catch (Exception ex) {
                message.getMessageProperties().setPriority(1);
            }
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        videos.forEach(video -> {
            CorrelationData correlationData = new CorrelationData("MessageId-" + video.getVideoId());
            rabbitTemplate.convertAndSend(Queue.Q_PRIORITY_MESSAGE, video, messagePostProcessor, correlationData);
        });
    }
}
