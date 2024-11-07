package org.example.service.consumer.exchange.fanout;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    // Injecting RabbitTemplate, which allows sending and receiving messages from RabbitMQ.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens for incoming messages from the Q_ORDER_PAYMENT queue.
     * It automatically consumes any messages placed in the queue and processes them.
     * The message will be converted into an 'Order' object and passed to the method.
     */
    @RabbitListener(
            // 'id' uniquely identifies this listener to the Q_ORDER_PAYMENT queue.
            id = org.example.constants.Queue.Q_ORDER_PAYMENT, // Listener ID for the Q_ORDER_PAYMENT queue.

            // 'ackMode' specifies how messages will be acknowledged.
            // 'AUTO' means RabbitMQ will automatically acknowledge the message once it's processed.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatically acknowledge messages after processing.

            // 'concurrency' defines the range of concurrent consumers.
            // The consumer pool will scale between 3 and 5 consumers processing messages concurrently.
            concurrency = "3-5", // Allows for 3 to 5 concurrent consumers to process messages in parallel.

            // 'bindings' define the relationship between the queue and the exchange,
            // and specify how the routing will be handled.
            bindings = @QueueBinding(
                    // Defining properties for the queue such as durability, exclusivity, and auto-deletion.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ORDER_PAYMENT, // The name of the queue.
                            durable = "true", // Ensures the queue survives RabbitMQ restarts.
                            exclusive = "false", // Queue is not exclusive and can be shared by other consumers.
                            autoDelete = "false", // Queue will not be deleted when there are no consumers.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Specifies the queue type as 'CLASSIC'.
                            }),

                    // The exchange that this queue is bound to.
                    // The exchange type is FANOUT, meaning messages will be broadcast to all queues bound to this exchange.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_ORDER, // The exchange used for routing messages.
                            type = ExchangeType.FANOUT, // FANOUT exchange type broadcasts the message to all bound queues.
                            durable = "true", // The exchange survives RabbitMQ restarts.
                            autoDelete = "false" // The exchange will not be deleted automatically.
                    )
            )
    )
    /**
     * This method is called when a message arrives in the Q_ORDER_PAYMENT queue.
     * The message will be automatically converted into an 'Order' object,
     * and the method processes it (in this case, it prints it to the console).
     */
    public void listenOrderPayment(Order order) {
        // Print the received order to the console. This is for logging and further processing.
        System.out.println(String.format("Queue : %s, Order : %s", org.example.constants.Queue.Q_ORDER_PAYMENT, order));
    }

    /**
     * This method demonstrates manual message consumption.
     * It uses the RabbitTemplate to fetch and convert a message from the Q_ORDER_PAYMENT queue.
     * It is a blocking call and returns null if there is an error or no message is available.
     */
    public Order consumeOrderPayment() {
        try {
            // Use RabbitTemplate to receive and convert the message from the queue into an 'Order' object.
            Order order = (Order) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ORDER_PAYMENT);
            return order; // Return the converted Order object to be further processed.
        } catch (Exception ex) {
            // If an error occurs (e.g., no message available or conversion failure), return null.
            return null; // Return null in case of an error or if no message is available.
        }
    }
}
