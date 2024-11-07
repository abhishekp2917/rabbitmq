package org.example.service.consumer.advance.multipleMessageTypes;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Order;
import org.example.model.Payment;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

/**
 * This service listens for messages from a topic exchange, specifically targeting purchase order and payment messages.
 * It processes these multiple type messages from the queue and also handles any default messages that may not match the specified types.
 */
@RabbitListener(
        id = org.example.constants.Queue.Q_TRANSACTION_PURCHASE, // The unique listener identifier for this consumer
        ackMode = ConsumerAcknowledgementMode.AUTO, // Automatic acknowledgment mode, indicating that messages will be automatically acknowledged after being processed
        concurrency = "3-5", // Configures the number of consumers to run in parallel. Here, the listener will have a concurrency of 3 to 5 threads handling the messages
        bindings = @QueueBinding(
                // Define the queue and its configuration
                value = @Queue(
                        value = org.example.constants.Queue.Q_TRANSACTION_PURCHASE, // The name of the queue from which messages will be consumed
                        durable = "true", // The queue will survive server restarts as it's marked durable
                        exclusive = "false", // The queue will not be exclusive to this listener, so other consumers can connect to it as well
                        autoDelete = "false", // The queue will not be automatically deleted after use, remaining available for future consumers
                        arguments = {
                                @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Specifying the queue type as CLASSIC (standard queue)
                        }),
                // Bind this queue to the specified exchange and configure the exchange settings
                exchange = @Exchange(
                        value = org.example.constants.Exchange.X_TRANSACTION, // The exchange to which the queue is bound
                        type = ExchangeType.TOPIC, // The exchange type is TOPIC, meaning it will route messages based on routing key patterns
                        durable = "true", // The exchange is durable and will persist across server restarts
                        autoDelete = "false" // The exchange will not be automatically deleted when no queues are bound to it
                ),
                key = "#.purchase.#" // The routing key pattern for this consumer. It will match any routing key with '.purchase.' anywhere in the key
        )
)
@Service
public class PurchaseTransactionConsumer {

    /**
     * Listens for messages of type Order from the queue.
     * This method processes the purchase order message when it is received.
     *
     * @param order The Order object received from the queue
     */
    @RabbitHandler
    public void listenPurchaseOrder(Order order) {
        // Log the received order for debugging purposes or further processing
        System.out.println(String.format("Queue : %s, Order : %s", org.example.constants.Queue.Q_TRANSACTION_PURCHASE, order));
    }

    /**
     * Listens for messages of type Payment from the queue.
     * This method processes the payment message when it is received.
     *
     * @param payment The Payment object received from the queue
     */
    @RabbitHandler
    public void listenPurchasePayment(Payment payment) {
        // Log the received payment for debugging purposes or further processing
        System.out.println(String.format("Queue : %s, Payment : %s", org.example.constants.Queue.Q_TRANSACTION_PURCHASE, payment));
    }

    /**
     * Default handler method to catch all messages that do not match the specific handler methods above.
     * This is a fallback method and is triggered for any unknown message types.
     *
     * @param object The generic message object that does not match any specific type
     */
    @RabbitHandler(isDefault = true)
    public void listenPurchaseDefault(Object object) {
        // Log and process any unexpected or generic message types
        System.out.println(String.format("Queue : %s, Object : %s", org.example.constants.Queue.Q_TRANSACTION_PURCHASE, object));
    }
}
