package org.example.service.producer.exchange.headers;

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

    /**
     * Publishes an FNOLClaim message to the specified headers exchange.
     *
     * @param exchangeName The name of the RabbitMQ exchange to send the message to.
     * @param fnolClaim The FNOLClaim object to be sent to the exchange.
     */
    public void publishFNOLClaim(String exchangeName, FNOLClaim fnolClaim) {
        // Create a map of headers based on the FNOLClaim properties
        Map<String, Object> headers = new HashMap<>();
        headers.put("fatal", fnolClaim.getFatal() ? "yes" : "no");
        headers.put("livable", fnolClaim.getLivable() ? "yes" : "no");
        headers.put("damage", fnolClaim.getDamage() ? "yes" : "no");

        // MessagePostProcessor modifies message properties before sending
        MessagePostProcessor messagePostProcessor = message -> {
            // Add custom headers
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().getHeaders().putAll(headers);

            // Set delivery mode to PERSISTENT for message durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set message priority
            message.getMessageProperties().setPriority(1);

            // Set content encoding and content type for proper message handling
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            return message;
        };

        // Send FNOLClaim message to the specified headers exchange
        rabbitTemplate.convertAndSend(exchangeName, "", fnolClaim, messagePostProcessor);
    }
}
