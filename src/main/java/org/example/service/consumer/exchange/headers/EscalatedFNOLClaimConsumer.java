package org.example.service.consumer.exchange.headers;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.FNOLClaim;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EscalatedFNOLClaimConsumer {

    // Injecting RabbitTemplate, which allows sending and receiving messages from RabbitMQ.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens for messages from the Q_CLAIM_ESCALATED queue.
     * The queue is bound to a HEADERs exchange type, which uses message headers for routing rather than routing keys.
     */
    @RabbitListener(
            // 'id' uniquely identifies this listener to the Q_CLAIM_ESCALATED queue.
            id = org.example.constants.Queue.Q_CLAIM_ESCALATED, // Listener ID for the Q_CLAIM_ESCALATED queue.

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
                            value = org.example.constants.Queue.Q_CLAIM_ESCALATED, // The name of the queue.
                            durable = "true", // Ensures the queue survives RabbitMQ restarts.
                            exclusive = "false", // Queue is not exclusive and can be shared by other consumers.
                            autoDelete = "false", // Queue will not be deleted when there are no consumers.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Specifies the queue type as 'CLASSIC'.
                            }),

                    // The exchange that this queue is bound to.
                    // The exchange type is HEADERS, meaning routing decisions are based on message headers.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_CLAIM, // The exchange used for routing messages.
                            type = ExchangeType.HEADERS, // HEADERS exchange type is used for routing based on message headers.
                            durable = "true", // The exchange survives RabbitMQ restarts.
                            autoDelete = "false" // The exchange will not be deleted automatically.
                    ),

                    // The routing logic for HEADERs exchanges is defined by the message's headers.
                    // 'x-match' is set to 'any', meaning the message will be routed to this queue if any of the specified headers match.
                    arguments = {
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "x-match", value = "any"), // Match if any header matches.
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "livable", value = "no"), // Only route messages with the header 'livable=no'.
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "fatal", value = "yes") // Only route messages with the header 'fatal=yes'.
                    }
            )
    )
    /**
     * This method is called when a message arrives in the Q_CLAIM_ESCALATED queue.
     * The message is automatically converted into an 'FNOLClaim' object and passed to the method for processing.
     */
    public void listenEscalatedFNOLClaim(FNOLClaim fnolClaim) {
        // Log the received FNOLClaim object, useful for tracking and debugging.
        System.out.println(String.format("Queue : %s : %s", org.example.constants.Queue.Q_CLAIM_ESCALATED, fnolClaim));
    }

    /**
     * This method demonstrates manual message consumption.
     * It uses the RabbitTemplate to fetch and convert a message from the Q_CLAIM_ESCALATED queue.
     * It is a blocking call and returns null if there is an error or no message is available.
     */
    public FNOLClaim consumeEscalatedFNOLClaim() {
        try {
            // Use RabbitTemplate to receive and convert the message from the queue into an 'FNOLClaim' object.
            FNOLClaim fnolClaim = (FNOLClaim) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_CLAIM_ESCALATED);
            return fnolClaim; // Return the converted FNOLClaim object to be further processed.
        } catch (Exception ex) {
            // If an error occurs (e.g., no message available or conversion failure), return null.
            return null; // Return null in case of an error or if no message is available.
        }
    }
}
