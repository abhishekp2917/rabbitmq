package org.example.service.producer.exchange.direct;

import org.example.model.Booking;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

/**
 * Service class responsible for publishing booking messages to a RabbitMQ exchange.
 * This class uses direct exchange routing to send the booking details to the specified routing key.
 */
@Service
public class BookingProducer {

    // Autowired RabbitTemplate to interact with RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a booking message to the specified RabbitMQ exchange with a routing key.
     * Custom message properties like delivery mode, priority, content encoding, and content type are set.
     *
     * @param exchangeName The name of the RabbitMQ exchange to send the message to.
     * @param routingKey The routing key used by RabbitMQ to route the message to the appropriate queue.
     * @param booking The Booking object to be sent to the exchange.
     */
    public void publishBooking(String exchangeName, String routingKey, Booking booking) {
        // MessagePostProcessor is used to modify message properties before sending it
        MessagePostProcessor messagePostProcessor = message -> {
            // Setting a custom header to identify the user sending the message
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Setting delivery mode to PERSISTENT to ensure messages survive broker crashes
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Setting the priority of the message to 1 (default priority)
            message.getMessageProperties().setPriority(1);

            // Setting content encoding to UTF-8 for proper handling of special characters
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Specifying the content type as JSON
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            return message;
        };

        // Sending the booking object to the specified exchange and routing key
        rabbitTemplate.convertAndSend(exchangeName, routingKey, booking, messagePostProcessor);
    }
}
