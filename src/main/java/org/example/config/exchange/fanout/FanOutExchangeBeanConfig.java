package org.example.config.exchange.fanout;

import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up a fanout exchange with multiple queues.
 *
 * A fanout exchange broadcasts messages to all bound queues without filtering by routing key,
 * making it useful for scenarios where all listeners should receive the same message, such as notifications.
 */
@Configuration
public class FanOutExchangeBeanConfig {

    /**
     * Defines a queue for order notification messages.
     *
     * - Queue type: Classic for standard message handling.
     * - Durable: Ensures persistence so that messages survive broker restarts.
     *
     * @return Queue object for order notifications.
     */
    @Bean
    public Queue orderNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Standard classic queue type
        return new Queue(org.example.constants.Queue.Q_ORDER_NOTIFICATION, true, false, false, args);
    }

    /**
     * Defines a queue for order payment messages.
     *
     * - Queue type: Classic for standard message handling.
     * - Durable: Ensures persistence so that messages survive broker restarts.
     *
     * @return Queue object for order payments.
     */
    @Bean
    public Queue orderPaymentQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Standard classic queue type
        return new Queue(org.example.constants.Queue.Q_ORDER_PAYMENT, true, false, false, args);
    }

    /**
     * Defines a fanout exchange for broadcasting order-related messages.
     *
     * - Fanout Exchange: This type of exchange ignores routing keys and sends messages to all bound queues.
     * - Durable: This ensures the exchange survives broker restarts.
     *
     * @return FanoutExchange object for broadcasting messages.
     */
    @Bean
    public FanoutExchange orderExchange() {
        return new FanoutExchange(Exchange.X_ORDER, true, false);
    }

    /**
     * Binds the `orderNotificationQueue` to the `orderExchange`.
     *
     * - Fanout binding: Every message sent to the `orderExchange` will be routed to `orderNotificationQueue`.
     *
     * @return Binding for `orderNotificationQueue` to `orderExchange`.
     */
    @Bean
    public Binding fanOutBindingOne() {
        return BindingBuilder.bind(orderNotificationQueue()).to(orderExchange());
    }

    /**
     * Binds the `orderPaymentQueue` to the `orderExchange`.
     *
     * - Fanout binding: Every message sent to the `orderExchange` will be routed to `orderPaymentQueue`.
     *
     * @return Binding for `orderPaymentQueue` to `orderExchange`.
     */
    @Bean
    public Binding fanOutBindingTwo() {
        return BindingBuilder.bind(orderPaymentQueue()).to(orderExchange());
    }
}
