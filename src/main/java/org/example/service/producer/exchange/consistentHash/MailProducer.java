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

@Service
public class MailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishMails(List<Mail> mails) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        mails.forEach(mail -> {
            rabbitTemplate.convertAndSend(Exchange.X_MAIL, mail.getTo(), mail, messagePostProcessor);
        });
    }
}
