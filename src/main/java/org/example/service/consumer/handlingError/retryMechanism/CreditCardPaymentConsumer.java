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
    private RabbitTemplate rabbitTemplate; // RabbitTemplate to send/receive messages from RabbitMQ.

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for converting JSON messages to Java objects.

    @Autowired
    private DLXErrorHandlingProcessor dlxErrorHandlingProcessor; // Custom processor to handle errors and retry logic.

    /**
     * Listens to the credit card payment queue for incoming messages and processes them.
     *
     * @param message The RabbitMQ message containing the payment data.
     * @param channel The RabbitMQ channel to acknowledge or reject messages.
     * @param deliveryTag The delivery tag of the message, used for manual acknowledgment.
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD, // Unique identifier for this listener.
            ackMode = ConsumerAcknowledgementMode.MANUAL, // Manual acknowledgment of messages.
            concurrency = "3-5", // The number of concurrent consumers allowed.
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD, // Queue for credit card payments.
                            durable = "true", // Ensure the queue is durable (survives RabbitMQ restarts).
                            exclusive = "false", // Queue can be shared among multiple consumers.
                            autoDelete = "false", // Queue won't auto-delete when no consumers are attached.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC), // Use classic queue type.
                                    @Argument(name = "x-dead-letter-exchange", value = org.example.constants.Exchange.X_PAYMENT_WAIT) // DLX for failed messages.
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_PAYMENT, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"), // Binding to the payment exchange.
                    key = "card.credit" // Routing key for the queue.
            )
    )
    public void listenCreditCardPayment(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // Deserialize the message body to a Payment object.
            Payment payment = objectMapper.readValue(message.getBody(), Payment.class);

            // Check if the payment method is "CREDIT_CARD".
            if(!payment.getPaymentMethod().equals("CREDIT_CARD")) {
                throw new AmqpRejectAndDontRequeueException("Invalid method type");
            }

            // If everything is fine, process the payment (logging it in this case).
            System.out.println(String.format("Queue : %s, Payment : %s", org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD, payment));

            // Acknowledge the message as successfully processed.
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception ex) {
            // Handle any errors, such as invalid payment method or deserialization issues.
            // This method handles requeueing the message, sending it to DLX, or retrying the message.
            dlxErrorHandlingProcessor.handleErrorMessage(message, channel, deliveryTag);
        }
    }
}
