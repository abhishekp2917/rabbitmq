package org.example.service.consumer.exchange.direct;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Booking;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventBookingConsumer {

    // Injecting RabbitTemplate to interact with RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens to a specific RabbitMQ queue, processes incoming messages, and acknowledges them.
     * The RabbitListener annotation defines how the consumer will listen to and process messages.
     */
    @RabbitListener(
            // The 'id' here corresponds to the queue to which this consumer is bound.
            // It's used to identify the listener for managing connections.
            id = org.example.constants.Queue.Q_BOOKING_EVENTS,

            // 'ackMode' defines the acknowledgement strategy. Here, AUTO means the message will be acknowledged automatically after it is processed.
            ackMode = ConsumerAcknowledgementMode.AUTO,

            // 'concurrency' specifies the number of consumer threads to handle messages.
            // It defines a range, meaning RabbitMQ can spawn between 3 to 5 threads to process the messages.
            concurrency = "3-5",

            // The 'bindings' section binds the listener to specific queues and exchanges.
            bindings = @QueueBinding(
                    // Define the queue associated with this listener. The arguments specify configuration options for the queue.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_BOOKING_EVENTS, // Queue name
                            durable = "true", // Queue will survive broker restarts
                            exclusive = "false", // Queue can be shared across multiple consumers
                            autoDelete = "false", // Queue will not be automatically deleted when no consumers are connected
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // This is a custom argument to specify the queue type.
                            }),

                    // Defines the exchange to which the queue is bound. Here, we are using a direct exchange.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_BOOKING, // Exchange name
                            type = ExchangeType.DIRECT, // Type of exchange (direct means messages are routed to queues based on routing keys)
                            durable = "true", // Exchange survives broker restarts
                            autoDelete = "false" // Exchange is not automatically deleted when no consumers are connected
                    ),

                    // 'key' defines the routing keys that this consumer will listen to.
                    // Only messages with these keys will be delivered to this queue.
                    key = {"concert.music", "match.ipl"} // Routing keys for the events
            )
    )
    /**
     * This method is invoked when a message is received on the specified queue. It will automatically
     * deserialize the message into a Booking object and process it.
     * The message is printed to the console for logging purposes.
     */
    public void listenEventBooking(Booking booking) {
        // Log the incoming booking event for debugging or auditing purposes
        System.out.println(String.format("Queue : %s, Booking : %s", org.example.constants.Queue.Q_BOOKING_EVENTS, booking));
    }

    /**
     * This method is used to manually consume messages from the specified queue.
     * It uses RabbitTemplate to fetch and convert the message from the queue into a Booking object.
     * In case of any exception (e.g., the queue is empty), it returns null.
     */
    public Booking consumeEventBooking() {
        try {
            // Fetch the next message from the queue and convert it to a Booking object
            Booking booking = (Booking) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_BOOKING_EVENTS);
            return booking;
        }
        catch (Exception ex) {
            // If something goes wrong (e.g., no message in the queue), return null
            return null;
        }
    }
}
