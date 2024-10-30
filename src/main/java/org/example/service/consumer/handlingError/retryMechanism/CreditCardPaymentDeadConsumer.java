package org.example.service.consumer.handlingError.retryMechanism;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Payment;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardPaymentDeadConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD,
            ackMode = ConsumerAcknowledgementMode.MANUAL,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC),
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_PAYMENT_DEAD, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"),
                    key = "card.credit"
            )
    )
    public void listenCreditCardPaymentDead(Payment payment) {
        System.out.println(String.format("Queue : %s, Payment : %s", org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD, payment));
    }
}
