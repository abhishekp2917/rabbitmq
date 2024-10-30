package org.example.service.consumer.advance.multipleMessageTypes;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Order;
import org.example.model.Payment;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RabbitListener(
        id = org.example.constants.Queue.Q_TRANSACTION_PURCHASE,
        ackMode = ConsumerAcknowledgementMode.AUTO,
        concurrency = "3-5",
        bindings = @QueueBinding(
                value = @Queue(
                        value = org.example.constants.Queue.Q_TRANSACTION_PURCHASE,
                        durable = "true",
                        exclusive = "false",
                        autoDelete = "false",
                        arguments = {
                                @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                        }),
                exchange = @Exchange(value = org.example.constants.Exchange.X_TRANSACTION, type = ExchangeType.TOPIC, durable = "true", autoDelete = "false"),
                key = "#.purchase.#"
        )
)
@Service
public class PurchaseTransactionConsumer {

    @RabbitHandler
    public void listenPurchaseOrder(Order order) {
        System.out.println(String.format("Queue : %s, Order : %s", org.example.constants.Queue.Q_TRANSACTION_PURCHASE, order));
    }

    @RabbitHandler
    public void listenPurchasePayment(Payment payment) {
        System.out.println(String.format("Queue : %s, Payment : %s", org.example.constants.Queue.Q_TRANSACTION_PURCHASE, payment));
    }

    @RabbitHandler(isDefault = true)
    public void listenPurchaseDefault(Object object) {
        System.out.println(String.format("Queue : %s, Object : %s", org.example.constants.Queue.Q_TRANSACTION_PURCHASE, object));
    }
}
