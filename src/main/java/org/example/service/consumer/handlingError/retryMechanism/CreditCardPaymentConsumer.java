package org.example.service.consumer.handlingError.retryMechanism;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Payment;
import org.example.util.handlingError.retryMechanism.DLXErrorHandlingProcessor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class CreditCardPaymentConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DLXErrorHandlingProcessor dlxErrorHandlingProcessor;


    @RabbitListener(
            id = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD,
            ackMode = ConsumerAcknowledgementMode.MANUAL,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC),
                                    @Argument(name = "x-dead-letter-exchange", value = org.example.constants.Exchange.X_PAYMENT_WAIT)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_PAYMENT, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"),
                    key = "card.credit"
            )
    )
    public void listenCreditCardPayment(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            Payment payment = objectMapper.readValue(message.getBody(), Payment.class);
            if(!payment.getPaymentMethod().equals("CREDIT_CARD")) {
                throw new AmqpRejectAndDontRequeueException("Invalid method type");
            }
            System.out.println(String.format("Queue : %s, Payment : %s", org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD, payment));
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception ex) {
            dlxErrorHandlingProcessor.handleErrorMessage(message, channel, deliveryTag);
        }
    }
}
