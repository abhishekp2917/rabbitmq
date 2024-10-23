package org.example.producer.exchange.headers;

import org.example.model.FNOLClaim;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class FNOLClaimProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishFNOLClaim(String exchangeName, FNOLClaim fnolClaim) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("fatal", fnolClaim.getFatal()?"yes":"no");
        headers.put("livable", fnolClaim.getLivable()?"yes":"no");
        headers.put("damage", fnolClaim.getDamage()?"yes":"no");
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().getHeaders().putAll(headers);
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        rabbitTemplate.convertAndSend(exchangeName, "", fnolClaim, messagePostProcessor);
    }
}
