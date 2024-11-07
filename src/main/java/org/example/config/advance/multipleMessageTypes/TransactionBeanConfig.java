package org.example.config.advance.multipleMessageTypes;

import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for defining beans related to transaction message handling.
 * This configuration sets up queues, exchanges, and bindings for handling purchase
 * transactions within a messaging system using RabbitMQ.
 *
 * The purpose of this configuration is to:
 * - Define a dedicated queue for purchase transactions.
 * - Establish a topic exchange for routing purchase transaction messages based on patterns.
 * - Bind the purchase transaction queue to the exchange with a specific routing key pattern.
 *
 * This approach allows for modular, configurable message routing based on the nature
 * and type of transaction.
 */
@Configuration
public class TransactionBeanConfig {

    /**
     * Defines the purchase transaction queue bean.
     *
     * This queue is configured with the following properties:
     * - `x-queue-type`: Set to `CLASSIC` to specify the RabbitMQ queue type.
     * - Durable: The queue is durable (persistent), meaning it survives a broker restart.
     * - Exclusive: Set to `false`, allowing multiple connections to use the queue.
     * - Auto-delete: Set to `false`, ensuring the queue is not deleted when no longer in use.
     *
     * @return Queue object representing the purchase transaction queue.
     */
    @Bean
    public Queue purchaseTransactionQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Specifies the queue type as 'classic' (default type)
        return new Queue(org.example.constants.Queue.Q_TRANSACTION_PURCHASE, true, false, false, args);
    }

    /**
     * Defines the topic exchange bean for purchase transactions.
     *
     * A topic exchange allows routing messages based on a routing key pattern. This
     * exchange is durable (persistent) and not auto-deletable. The exchange is shared
     * among all relevant queues for transaction-based message routing.
     *
     * @return TopicExchange object for routing purchase-related messages.
     */
    @Bean
    public TopicExchange purchaseExchange() {
        return new TopicExchange(Exchange.X_TRANSACTION, true, false);
    }

    /**
     * Creates a binding between the purchase transaction queue and the purchase exchange.
     *
     * The binding uses a routing key pattern `#.purchase.#`:
     * - `#` is a wildcard that matches zero or more words, allowing flexibility in routing keys.
     * - `purchase` specifies that the binding applies to messages with "purchase" in the routing key.
     *
     * This setup ensures that messages related to purchase transactions are routed to the
     * purchase transaction queue.
     *
     * @return Binding object that links the purchase transaction queue to the exchange.
     */
    @Bean
    public Binding purchaseTransactionBinding() {
        return BindingBuilder.bind(purchaseTransactionQueue()).to(purchaseExchange()).with("#.purchase.#");
    }

}
