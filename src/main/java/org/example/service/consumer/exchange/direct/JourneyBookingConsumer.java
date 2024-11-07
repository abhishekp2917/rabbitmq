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
public class JourneyBookingConsumer {

    // Injecting RabbitTemplate to enable interaction with RabbitMQ.
    // RabbitTemplate allows sending and receiving messages from queues, which is essential in message-driven applications.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens for messages (Booking objects) from the Q_BOOKING_JOURNEY queue.
     * When a message arrives on this queue, the method is invoked.
     *
     * @RabbitListener listens to the defined queue and triggers the method when a message is available.
     */
    @RabbitListener(
            // 'id' specifies the unique listener identifier. This will map to the queue `Q_BOOKING_JOURNEY`.
            id = org.example.constants.Queue.Q_BOOKING_JOURNEY, // Listener ID for the Q_BOOKING_JOURNEY queue.

            // 'ackMode' specifies the acknowledgment mode. AUTO means that the message will be acknowledged after it is processed successfully.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatically acknowledge messages after processing.

            // 'concurrency' specifies the range of concurrent consumers for this listener.
            // This helps scale the message consumption. For example, "3-5" means 3 to 5 consumers can process messages simultaneously.
            concurrency = "3-5", // Allows for 3-5 concurrent consumers for better load handling.

            // 'bindings' defines the bindings between the queue, the exchange, and the routing keys.
            bindings = @QueueBinding(
                    // Defining the properties of the queue where messages will be stored.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_BOOKING_JOURNEY, // Queue name, specifying where the messages will be placed.
                            durable = "true", // Ensures that the queue survives RabbitMQ restarts.
                            exclusive = "false", // The queue is shared, meaning other consumers can also listen to it.
                            autoDelete = "false", // The queue will not be deleted automatically when no consumers are connected.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Defines the type of queue (Classic in this case).
                            }),

                    // Binding the queue to an exchange with the type 'direct'. This means that only messages with a matching routing key will be routed to this queue.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_BOOKING, // The name of the exchange (X_BOOKING).
                            type = ExchangeType.DIRECT, // Type of exchange, in this case, 'DIRECT' which is a one-to-one routing based on the routing key.
                            durable = "true", // Ensures the exchange persists across RabbitMQ restarts.
                            autoDelete = "false" // The exchange will not be deleted when no queues are bound to it.
                    ),

                    // The routing keys that are used by the direct exchange to route messages to the bound queues.
                    key = {"flight", "train"} // These routing keys will match messages sent with "flight" or "train".
            )
    )
    /**
     * This method is triggered whenever a message (a Booking object) arrives at the Q_BOOKING_JOURNEY queue.
     * The message is processed and logged for debugging or processing purposes.
     */
    public void listenJourneyBooking(Booking booking) {
        // Here we print the received booking details from the queue. This would typically be processed further.
        // The 'booking' is a model object, and its details are being outputted.
        System.out.println(String.format("Queue : %s, Booking : %s", org.example.constants.Queue.Q_BOOKING_JOURNEY, booking));
    }

    /**
     * This method manually consumes a message from the Q_BOOKING_JOURNEY queue using the RabbitTemplate.
     *
     * It demonstrates how to programmatically retrieve messages from the queue.
     * If no message is available, or if an error occurs, it will return null.
     */
    public Booking consumeJourneyBooking() {
        try {
            // Attempt to consume and convert the message from the Q_BOOKING_JOURNEY queue into a Booking object.
            // This method uses 'rabbitTemplate.receiveAndConvert()', which retrieves the message and automatically deserializes it into a Booking object.
            Booking booking = (Booking) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_BOOKING_JOURNEY);
            return booking; // Return the Booking object, which will be used in further processing.
        } catch (Exception ex) {
            // In case of any errors during the message consumption (e.g., no messages in the queue or deserialization issues),
            // we return null. You might want to handle specific error cases in production code.
            return null; // Return null if there's an exception or no message in the queue.
        }
    }
}
