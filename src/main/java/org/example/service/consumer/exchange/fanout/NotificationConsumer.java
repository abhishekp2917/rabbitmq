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
public class NotificationConsumer {

    // Injecting RabbitTemplate which is the main class used to interact with RabbitMQ.
    // It allows us to send and receive messages from RabbitMQ queues.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens for incoming messages from the Q_ORDER_NOTIFICATION queue.
     * The listener automatically consumes messages sent to this queue.
     * The method will be triggered whenever a message arrives in the queue.
     */
    @RabbitListener(
            // 'id' provides a unique identifier for the listener that is tied to the Q_ORDER_NOTIFICATION queue.
            id = org.example.constants.Queue.Q_ORDER_NOTIFICATION, // Listener ID for the Q_ORDER_NOTIFICATION queue.

            // 'ackMode' specifies how the message acknowledgment will be handled.
            // Here it is set to AUTO, meaning RabbitMQ will automatically acknowledge the message once it's processed.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatically acknowledge messages after processing.

            // 'concurrency' allows you to specify a range of concurrent consumers.
            // This helps scale the processing of messages by allowing multiple consumers to process messages in parallel.
            concurrency = "3-5", // This configuration allows for 3 to 5 consumers at once for improved throughput.

            // 'bindings' define how the queue is connected to the exchange and the routing mechanism.
            bindings = @QueueBinding(
                    // Defines the properties for the queue. This includes attributes like durability, exclusivity, etc.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ORDER_NOTIFICATION, // The name of the queue.
                            durable = "true", // The queue will survive RabbitMQ restarts.
                            exclusive = "false", // The queue is not exclusive and can be shared by other consumers.
                            autoDelete = "false", // The queue will not be automatically deleted when no consumers are connected.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // The queue type is set to CLASSIC here.
                            }),

                    // The exchange this queue is bound to.
                    // The FANOUT exchange type means that messages are broadcast to all bound queues, without regard for routing keys.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_ORDER, // The exchange used for routing messages.
                            type = ExchangeType.FANOUT, // FANOUT exchange type broadcasts the message to all queues bound to it.
                            durable = "true", // The exchange will survive RabbitMQ restarts.
                            autoDelete = "false" // The exchange will not be automatically deleted.
                    )
            )
    )
    /**
     * This method is triggered when a message arrives in the Q_ORDER_NOTIFICATION queue.
     * The message will be converted into an 'Order' object and passed to the method.
     * The method prints the received Order object for further processing or logging.
     */
    public void listenOrderNotification(Order order) {
        // Print the received order to the console. This is just for logging the details of the order.
        System.out.println(String.format("Queue : %s, Order : %s", org.example.constants.Queue.Q_ORDER_NOTIFICATION, order));
    }

    /**
     * This method demonstrates how to manually consume a message from the Q_ORDER_NOTIFICATION queue.
     * It uses the RabbitTemplate to retrieve the message and convert it to an 'Order' object.
     */
    public Order consumeOrderNotification() {
        try {
            // Use the 'receiveAndConvert()' method to fetch the message from the queue and convert it into an Order object.
            Order order = (Order) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ORDER_NOTIFICATION);
            return order; // Return the converted Order object to be processed.
        } catch (Exception ex) {
            // In case an exception occurs (e.g., no message available, or conversion failure), return null.
            // You can enhance this with specific error handling if needed.
            return null; // Return null if there is an error or no message in the queue.
        }
    }
}
