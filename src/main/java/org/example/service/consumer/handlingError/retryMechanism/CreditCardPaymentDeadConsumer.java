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
    private RabbitTemplate rabbitTemplate; // RabbitTemplate is used to interact with RabbitMQ.

    /**
     * Listens to the dead-letter queue for credit card payments that couldn't be processed successfully.
     *
     * @param payment The Payment object containing the payment data.
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD, // Listener ID for the dead-letter queue.
            ackMode = ConsumerAcknowledgementMode.MANUAL, // Manual acknowledgment to control when the message is acknowledged.
            concurrency = "3-5", // Number of consumers allowed to run concurrently.
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD, // The name of the dead-letter queue.
                            durable = "true", // Ensures the queue survives RabbitMQ restarts.
                            exclusive = "false", // Queue can be shared among multiple consumers.
                            autoDelete = "false", // Queue won't be deleted automatically when no consumers are attached.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Classic queue type for the DLQ.
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_PAYMENT_DEAD, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"), // Binding to the dead-letter exchange.
                    key = "card.credit" // Routing key for the DLQ.
            )
    )
    public void listenCreditCardPaymentDead(Payment payment) {
        // Log the payment from the dead-letter queue to inspect or further process it.
        System.out.println(String.format("Queue : %s, Payment : %s", org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD, payment));
    }
}
