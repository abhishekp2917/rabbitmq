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

    public void publishPayment(List<Payment> payments) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("user", "Abhishek");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setPriority(1);
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.toString());
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        payments.forEach(payment -> rabbitStreamTemplate.convertAndSend(payment, messagePostProcessor));
    }
}
