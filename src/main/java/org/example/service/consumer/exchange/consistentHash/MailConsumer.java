package org.example.service.consumer.exchange.consistentHash;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Mail;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailConsumer {

    // Injecting RabbitTemplate to interact with RabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Listener for queue Q_MAIL_1. This method processes messages routed through the
     * consistent hash exchange based on routing keys ("concert.music" and "match.ipl").
     */
    @RabbitListener(
            // The 'id' corresponds to the queue name, which is used for identifying the listener.
            id = org.example.constants.Queue.Q_MAIL_1, // Listener ID for queue 1.

            // 'ackMode' determines the acknowledgement strategy for the message after processing.
            // AUTO means the message will be automatically acknowledged once processed.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatic message acknowledgment.

            // 'concurrency' sets the range of consumer threads that can be active concurrently.
            // Here it specifies 3-5 threads.
            concurrency = "3-5", // Allows for 3-5 concurrent consumers.

            // 'bindings' specifies the queue and exchange configurations for this listener.
            bindings = @QueueBinding(
                    // This defines the queue properties like its name, durability, etc.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_MAIL_1, // Name of the first queue.
                            durable = "true", // Queue will survive RabbitMQ restarts.
                            exclusive = "false", // Queue can be shared by other consumers.
                            autoDelete = "false", // Queue will not be automatically deleted.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Setting the queue type to CLASSIC.
                            }),

                    // Defines the exchange this queue is bound to. Here, we use a Consistent Hash exchange.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_MAIL, // The exchange name.
                            type = ExchangeType.X_CONSISTENT_HASH, // Consistent Hash exchange ensures that messages with the same routing key go to the same queue.
                            durable = "true", // Ensures the exchange survives RabbitMQ restarts.
                            autoDelete = "false" // The exchange will not be deleted automatically.
                    ),

                    // Routing keys specify which messages will be routed to this queue.
                    // In a consistent hash exchange, the routing key determines which queue will receive the message.
                    key = {"concert.music", "match.ipl"} // Routing keys for consistent hashing.
            )
    )
    /**
     * This method is invoked whenever a message (Mail object) is received on queue Q_MAIL_1.
     * It processes the mail by simply printing it to the console for now.
     */
    public void listenMailQueue1(Mail mail) {
        // Log the received mail for debugging or processing purposes
        System.out.println(String.format("Queue : %s, Mail : %s", org.example.constants.Queue.Q_MAIL_1, mail)); // Process the mail.
    }

    /**
     * Listener for queue Q_MAIL_2. Similar to the first listener, this method processes messages
     * routed through the same consistent hash exchange but for a different queue.
     */
    @RabbitListener(
            // The 'id' corresponds to the queue name for this specific listener.
            id = org.example.constants.Queue.Q_MAIL_2, // Listener ID for queue 2.

            // 'ackMode' determines the acknowledgement strategy for the message after processing.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatic message acknowledgment.

            // 'concurrency' sets the range of consumer threads for this listener.
            concurrency = "3-5", // Allows for 3-5 concurrent consumers.

            // 'bindings' specifies the queue and exchange configurations for this listener.
            bindings = @QueueBinding(
                    // This defines the queue properties like its name, durability, etc.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_MAIL_2, // Name of the second queue.
                            durable = "true", // Queue will survive RabbitMQ restarts.
                            exclusive = "false", // Queue can be shared by other consumers.
                            autoDelete = "false", // Queue will not be automatically deleted.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Setting the queue type to CLASSIC.
                            }),

                    // Defines the exchange this queue is bound to. Here, we use a Consistent Hash exchange.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_MAIL, // The exchange name.
                            type = ExchangeType.X_CONSISTENT_HASH, // Consistent Hash exchange ensures that messages with the same routing key go to the same queue.
                            durable = "true", // Ensures the exchange survives RabbitMQ restarts.
                            autoDelete = "false" // The exchange will not be deleted automatically.
                    ),

                    // Routing keys specify which messages will be routed to this queue.
                    // In a consistent hash exchange, the routing key determines which queue will receive the message.
                    key = {"concert.music", "match.ipl"} // Routing keys for consistent hashing.
            )
    )
    /**
     * This method is invoked whenever a message (Mail object) is received on queue Q_MAIL_2.
     * It processes the mail by simply printing it to the console for now.
     */
    public void listenMailQueue2(Mail mail) {
        // Log the received mail for debugging or processing purposes
        System.out.println(String.format("Queue : %s, Mail : %s", org.example.constants.Queue.Q_MAIL_2, mail)); // Process the mail.
    }
}
