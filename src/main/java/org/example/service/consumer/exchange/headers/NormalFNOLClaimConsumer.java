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
public class NormalFNOLClaimConsumer {

    // Injecting RabbitTemplate, which is used for sending and receiving messages from RabbitMQ.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens for messages from the Q_CLAIM_NORMAL queue.
     * The queue is bound to a HEADERs exchange type, which uses message headers for routing.
     */
    @RabbitListener(
            // 'id' is a unique identifier for the listener attached to the Q_CLAIM_NORMAL queue.
            id = org.example.constants.Queue.Q_CLAIM_NORMAL, // Listener ID for the Q_CLAIM_NORMAL queue.

            // 'ackMode' specifies how the message acknowledgment should occur.
            // AUTO means RabbitMQ will automatically acknowledge the message once it's consumed.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatic acknowledgment after message processing.

            // 'concurrency' defines the number of concurrent consumers to handle the messages.
            // Here, the system will use between 3 to 5 consumers for parallel processing.
            concurrency = "3-5", // 3 to 5 concurrent consumers.

            // 'bindings' define the relationship between the queue and the exchange.
            // This defines how the message will be routed from the exchange to the queue.
            bindings = @QueueBinding(
                    // Defining the queue properties like name, durability, exclusivity, and auto-deletion.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_CLAIM_NORMAL, // The queue name.
                            durable = "true", // Ensures the queue survives RabbitMQ restarts.
                            exclusive = "false", // The queue is not exclusive to a single consumer.
                            autoDelete = "false", // The queue won't be automatically deleted.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Specifies the queue type as CLASSIC.
                            }),

                    // The exchange this queue is bound to.
                    // The exchange type is HEADERs, meaning routing decisions are made based on headers in the message.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_CLAIM, // The exchange name.
                            type = ExchangeType.HEADERS, // The exchange type is HEADERs.
                            durable = "true", // Ensures the exchange survives RabbitMQ restarts.
                            autoDelete = "false" // The exchange won't be deleted automatically.
                    ),

                    // The routing logic for HEADERs exchanges is determined by the message headers.
                    // 'x-match' is set to 'all', meaning that all header conditions must match for routing.
                    arguments = {
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "x-match", value = "all"), // All header conditions must match.
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "livable", value = "yes"), // Only route messages with 'livable' header as 'yes'.
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "fatal", value = "no"), // Only route messages with 'fatal' header as 'no'.
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "damage", value = "yes") // Only route messages with 'damage' header as 'yes'.
                    }
            )
    )
    /**
     * This method is triggered when a message arrives in the Q_CLAIM_NORMAL queue.
     * The message is automatically converted into an 'FNOLClaim' object and passed to this method.
     */
    public void listenNormalFNOLClaim(FNOLClaim fnolClaim) {
        // Log the received FNOLClaim object. This is useful for tracking and debugging.
        System.out.println(String.format("Queue : %s, Claim : %s", org.example.constants.Queue.Q_CLAIM_NORMAL, fnolClaim));
    }

    /**
     * This method allows manual message consumption.
     * It uses RabbitTemplate to fetch and convert a message from the Q_CLAIM_NORMAL queue.
     * The method is blocking and returns null if there is an error or no message is available.
     */
    public FNOLClaim consumeNormalFNOLClaim() {
        try {
            // Attempt to receive and convert a message from the queue to an FNOLClaim object.
            FNOLClaim fnolClaim = (FNOLClaim) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_CLAIM_NORMAL);
            return fnolClaim; // Return the converted FNOLClaim object.
        } catch (Exception ex) {
            // If an error occurs (e.g., no message or conversion failure), return null.
            return null; // Return null if no message is available or if there's an error.
        }
    }
}
