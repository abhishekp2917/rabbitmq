package org.example.config.basic;

import org.example.constants.QueueType;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for defining a priority message queue.
 *
 * This configuration sets up a queue specifically designed to handle messages with priority levels,
 * allowing certain messages to be processed ahead of others based on priority.
 */
@Configuration
public class PriorityQueueBeanConfig {

    /**
     * Defines a priority queue for handling messages with varying priority levels.
     *
     * - Durable: This queue will persist even if the broker restarts.
     * - Classic queue type: Specified as "classic" for compatibility with standard RabbitMQ queues.
     * - Maximum priority: Configured to support up to 255 priority levels, with higher values processed first.
     *
     * Priority queues are useful in scenarios where certain messages need to be processed before others,
     * like time-sensitive notifications or urgent tasks.
     *
     * @return Queue object for priority-based message handling.
     */
    @Bean
    public Queue PriorityMessageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);  // Sets queue type to classic for standard handling
        args.put("x-max-priority", 255);              // Enables priority, with 255 as the maximum priority level
        return new Queue(org.example.constants.Queue.Q_PRIORITY_MESSAGE, true, false, false, args);
    }
}
