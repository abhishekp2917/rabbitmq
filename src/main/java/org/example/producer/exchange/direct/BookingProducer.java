package org.example.producer.exchange.direct;

import org.example.model.Booking;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class BookingProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishBooking(String exchangeName, String routingKey, Booking booking) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        rabbitTemplate.convertAndSend(exchangeName, routingKey, booking, messagePostProcessor);
    }
}
