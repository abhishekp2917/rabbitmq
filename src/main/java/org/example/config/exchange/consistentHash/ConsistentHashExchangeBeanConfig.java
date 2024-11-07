package org.example.config.exchange.consistentHash;

import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for defining a consistent hash exchange with multiple queues.
 *
 * This setup is designed for use cases where message distribution needs to be consistent across queues
 * based on a hash key, ensuring that messages with the same routing key go to the same queue.
 */
@Configuration
public class ConsistentHashExchangeBeanConfig {

    /**
     * Defines the first mail queue.
     *
     * - This queue is set up with a classic type for compatibility with standard RabbitMQ configurations.
     * - It is durable, so it persists even if the broker restarts.
     *
     * @return Queue object representing the first mail queue.
     */
    @Bean
    public Queue mailQueue1() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Classic queue type for standard handling
        return new Queue(org.example.constants.Queue.Q_MAIL_1, true, false, false, args);
    }

    /**
     * Defines the second mail queue.
     *
     * - Like the first queue, it is also durable and configured as a classic queue.
     * - This queue works alongside `mailQueue1` to handle message distribution based on consistent hashing.
     *
     * @return Queue object representing the second mail queue.
     */
    @Bean
    public Queue mailQueue2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Classic queue type for compatibility
        return new Queue(org.example.constants.Queue.Q_MAIL_2, true, false, false, args);
    }

    /**
     * Defines a consistent hash exchange for distributing mail messages.
     *
     * - This custom exchange type ensures that messages with the same routing key or hash key
     *   are consistently routed to the same queue, which can help with load balancing across queues.
     * - Durable, so it persists after broker restarts.
     *
     * @return CustomExchange object representing a consistent hash exchange.
     */
    @Bean
    public Exchange mailExchange() {
        return new CustomExchange(
                org.example.constants.Exchange.X_MAIL,
                ExchangeType.X_CONSISTENT_HASH,
                true, // Durable
                false // Not auto-deleted
        );
    }

    /**
     * Binds `mailQueue1` to the `mailExchange` with a consistent hash key of "3".
     *
     * - The binding key "3" specifies that messages hashed to this key will consistently route to `mailQueue1`.
     * - Helps distribute messages based on hash values, providing balanced processing across queues.
     *
     * @return Binding for `mailQueue1`.
     */
    @Bean
    public Binding mailBindingOne() {
        return BindingBuilder.bind(mailQueue1()).to(mailExchange()).with("3").noargs();
    }

    /**
     * Binds `mailQueue2` to the `mailExchange` with a consistent hash key of "2".
     *
     * - The binding key "2" indicates that messages hashed to this key will consistently route to `mailQueue2`.
     * - Allows for consistent distribution, balancing the load across `mailQueue1` and `mailQueue2`.
     *
     * @return Binding for `mailQueue2`.
     */
    @Bean
    public Binding mailBindingTwo() {
        return BindingBuilder.bind(mailQueue2()).to(mailExchange()).with("2").noargs();
    }
}
