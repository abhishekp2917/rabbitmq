package org.example.config.exchange.topic;

import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for a Topic Exchange setup for notification services.
 *
 * This setup uses a Topic Exchange to route messages based on routing patterns
 * that match specific queue requirements, making it suitable for different types of notifications.
 */
@Configuration
public class TopicExchangeBeanConfig {

    /**
     * Creates a queue for OTP notifications.
     *
     * - Queue type: Classic, for standard message processing.
     * - Durable: Enables message persistence across broker restarts.
     *
     * @return Queue for OTP notifications.
     */
    @Bean
    public Queue otpNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_NOTIFICATION_OTP, true, false, false, args);
    }

    /**
     * Creates a queue for payment notifications.
     *
     * - Queue type: Classic, ensuring standard delivery.
     * - Durable: Ensures the queue persists across restarts.
     *
     * @return Queue for payment notifications.
     */
    @Bean
    public Queue paymentNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_NOTIFICATION_PAYMENT, true, false, false, args);
    }

    /**
     * Creates a queue for subscriber notifications.
     *
     * - Queue type: Classic, for traditional queue behavior.
     * - Durable: Retains messages across restarts.
     *
     * @return Queue for subscriber notifications.
     */
    @Bean
    public Queue subscriberNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_NOTIFICATION_SUBSCRIBER, true, false, false, args);
    }

    /**
     * Defines a Topic Exchange for notification messages.
     *
     * - Durable: Ensures the exchange persists across broker restarts.
     *
     * @return TopicExchange for notifications.
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(org.example.constants.Exchange.X_NOTIFICATION, true, false);
    }

    /**
     * Binds the `otpNotificationQueue` to `notificationExchange` using a pattern that matches OTP-related messages.
     *
     * - Routing Pattern: `#.otp.#` - Matches any routing key that contains "otp" in any position.
     *
     * @return Binding for OTP notifications.
     */
    @Bean
    public Binding topicBindingOne() {
        return BindingBuilder.bind(otpNotificationQueue()).to(notificationExchange()).with("#.otp.#");
    }

    /**
     * Binds the `paymentNotificationQueue` to `notificationExchange` using a pattern specific to payment notifications.
     *
     * - Routing Pattern: `*.payment.*` - Matches routing keys where "payment" is in the second position.
     *
     * @return Binding for payment notifications.
     */
    @Bean
    public Binding topicBindingTwo() {
        return BindingBuilder.bind(paymentNotificationQueue()).to(notificationExchange()).with("*.payment.*");
    }

    /**
     * Binds the `subscriberNotificationQueue` to `notificationExchange` to receive messages related to subscriber notifications.
     *
     * - Routing Pattern: `subscriber.#` - Matches any routing key starting with "subscriber".
     *
     * @return Binding for subscriber notifications.
     */
    @Bean
    public Binding topicBindingThree() {
        return BindingBuilder.bind(subscriberNotificationQueue()).to(notificationExchange()).with("subscriber.#");
    }
}
