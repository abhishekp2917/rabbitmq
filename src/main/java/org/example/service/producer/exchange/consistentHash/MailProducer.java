package org.example.service.producer.exchange.consistentHash;

import org.example.constants.Exchange;
import org.example.model.Mail;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service class for publishing email messages to a RabbitMQ exchange using consistent hash routing.
 *
 * This class sends a list of mail objects to a specified RabbitMQ exchange using consistent hashing based on the mail's recipient address.
 * It sets custom properties like delivery mode, priority, content encoding, and content type.
 */
@Service
public class MailProducer {

    // Autowired RabbitTemplate to interact with RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a list of mail messages to a specified RabbitMQ exchange.
     * The routing is done based on the recipient address (consistent hash routing).
     * Custom message properties like priority, delivery mode, and encoding are set.
     *
     * @param mails List of Mail objects to be sent to the exchange.
     */
    public void publishMails(List<Mail> mails) {
        // MessagePostProcessor is used to modify message properties before sending
        MessagePostProcessor messagePostProcessor = message -> {
            // Setting a custom header to identify the user sending the message
            message.getMessageProperties().setHeader("user", "Abhishek");

            // Setting delivery mode to PERSISTENT, so the message survives broker crashes
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // Setting message priority to 1 (default priority)
            message.getMessageProperties().setPriority(1);

            // Setting content encoding to UTF-8 for proper handling of special characters
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());

            // Specifying the content type as JSON
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

            return message;
        };

        // For each mail in the list, send it to the exchange with the recipient address as routing key
        mails.forEach(mail -> {
            // The routing key is the recipient's email address (used for consistent hash routing)
            rabbitTemplate.convertAndSend(Exchange.X_MAIL, mail.getTo(), mail, messagePostProcessor);
        });
    }
}
