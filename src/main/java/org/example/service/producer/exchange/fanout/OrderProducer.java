package org.example.service.producer.exchange.fanout;

import org.example.model.Order;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class OrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes an order message to the specified RabbitMQ exchange.
     *
     * @param exchangeName The name of the RabbitMQ exchange to send the message to.
     * @param order The Order object to be sent to the exchange.
     */
    public void publishOrder(String exchangeName, Order order) {
        // MessagePostProcessor is used to modify message properties before sending the message
        MessagePostProcessor messagePostProcessor = message -> {
            // Set custom header to identify the user sending the message
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set delivery mode to PERSISTENT to ensure the message survives broker crashes
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set the priority of the message to 1 (default priority)
            message.getMessageProperties().setPriority(1);

            // Set content encoding to UTF-8 to ensure special characters are handled correctly
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Set content type to JSON since we are sending a JSON message
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            return message;
        };

        // Send the order object to the specified exchange with an empty routing key
        rabbitTemplate.convertAndSend(exchangeName, "", order, messagePostProcessor);
    }
}
