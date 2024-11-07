package org.example.service.producer.handlingError.retryMechanism;

import org.example.model.Payment;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a Payment message to the specified exchange with a routing key.
     *
     * @param exchangeName The name of the RabbitMQ exchange.
     * @param routingKey The routing key for message routing.
     * @param payment The Payment object to be sent.
     */
    public void publishPayment(String exchangeName, String routingKey, Payment payment) {
        // Configure message properties using MessagePostProcessor
        MessagePostProcessor messagePostProcessor = message -> {
            // Add custom header to the message
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set message delivery mode to PERSISTENT for durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set message priority
            message.getMessageProperties().setPriority(1);

            // Set message content encoding and content type for proper handling
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            return message;
        };

        // Send the payment object to the exchange using the specified routing key
        rabbitTemplate.convertAndSend(exchangeName, routingKey, payment, messagePostProcessor);
    }
}
