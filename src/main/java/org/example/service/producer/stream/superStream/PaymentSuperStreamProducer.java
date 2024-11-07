package org.example.service.producer.stream.superStream;

import org.example.constants.Stream;
import org.example.model.Payment;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class PaymentSuperStreamProducer {

    @Autowired
    @Qualifier(Stream.S_PAYMENT)
    private RabbitStreamTemplate rabbitStreamTemplate;

    /**
     * Publishes a list of payment objects to a RabbitMQ stream with custom message properties.
     *
     * @param payments The list of payment objects to be published.
     */
    public void publishPayment(List<Payment> payments) {
        // Define the MessagePostProcessor to customize message properties
        MessagePostProcessor messagePostProcessor = message -> {
            // Set the custom header with the key "user" and value "Abhishek"
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Set the message delivery mode to PERSISTENT to ensure message durability
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Set the message priority to 1 (lowest priority)
            message.getMessageProperties().setPriority(1);

            // Set the content encoding to UTF-8
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Set the content type to JSON
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // Return the modified message with set properties
            return message;
        };

        // Loop through each payment object and publish them to the stream
        payments.forEach(payment ->
                // Convert and send each payment object to the specified stream
                rabbitStreamTemplate.convertAndSend(payment, messagePostProcessor)
        );
    }
}
