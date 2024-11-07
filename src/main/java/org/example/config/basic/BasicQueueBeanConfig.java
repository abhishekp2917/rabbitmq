package org.example.config.basic;

import org.example.constants.QueueType;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for defining basic message queues.
 *
 * This configuration sets up two queues:
 * - One for handling plain text messages.
 * - One for handling JSON format messages.
 *
 * Both queues are:
 * - Durable: They will survive a broker restart.
 * - Configured as classic queues, suitable for general-purpose message handling.
 */
@Configuration
public class BasicQueueBeanConfig {

    /**
     * Defines a queue for handling text messages.
     *
     * - Durable: The queue is persistent and remains available after broker restarts.
     * - Classic queue type: Configured with "x-queue-type" as "classic" for compatibility with
     *   standard RabbitMQ settings.
     *
     * @return Queue object for plain text messages.
     */
    @Bean
    public Queue textMessageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Defines the queue type as classic for standard message handling
        return new Queue(org.example.constants.Queue.Q_MESSAGE_TEXT, true, false, false, args);
    }

    /**
     * Defines a queue for handling JSON messages.
     *
     * - Durable: Ensures the queue persists through broker restarts.
     * - Classic queue type: Also set with "x-queue-type" as "classic" for compatibility.
     *
     * This queue is specifically designated to handle messages in JSON format, which could be useful
     * for structured data or serialized objects.
     *
     * @return Queue object for JSON messages.
     */
    @Bean
    public Queue jsonMessageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Specifies classic queue type for compatibility with RabbitMQ defaults
        return new Queue(org.example.constants.Queue.Q_MESSAGE_JSON, true, false, false, args);
    }
}
