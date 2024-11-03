package org.example.service.producer.advance.prefetch;

import org.example.constants.Exchange;
import org.example.model.Audio;
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
public class AudioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishAudios(List<Audio> audios) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        for(Audio audio : audios) {
            CorrelationData correlationData = new CorrelationData("MessageId-" + System.currentTimeMillis());
            rabbitTemplate.convertAndSend(Exchange.X_AUDIO, "", audio, messagePostProcessor, correlationData);
        }
    }
}
