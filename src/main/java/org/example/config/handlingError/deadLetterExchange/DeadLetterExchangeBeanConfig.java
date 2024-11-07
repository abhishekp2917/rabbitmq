package org.example.config.handlingError.deadLetterExchange;

import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for managing Dead Letter Exchange (DLX) handling in video encoding processes.
 *
 * This setup includes both the primary exchange and queue for video encoding tasks, as well as
 * a dead-letter mechanism for handling failed or expired messages. The DLX is a common design
 * pattern in message-oriented middleware to process undelivered messages, ensuring robustness
 * and reliability in message processing.
 */
@Configuration
public class DeadLetterExchangeBeanConfig {

    /**
     * Configures the primary queue for video encoding tasks.
     *
     * - Queue type: Classic, suitable for standard message handling.
     * - x-dead-letter-exchange: Specifies the exchange to which messages will be routed if they
     *   are rejected, unacknowledged, or expire (i.e., they are dead-lettered).
     * - x-message-ttl: Sets the Time-To-Live (TTL) for messages in milliseconds. Messages that
     *   exceed this time are moved to the DLX.
     *
     * @return A durable queue for handling video encoding tasks with dead-letter capabilities.
     */
    @Bean
    public Queue videoEncodingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        args.put("x-dead-letter-exchange", Exchange.X_ENCODING_DLX);
        args.put("x-message-ttl", 10000); // Message TTL set to 10 seconds.
        return new Queue(org.example.constants.Queue.Q_ENCODING_VIDEO, true, false, false, args);
    }

    /**
     * Configures the dead-letter queue for handling expired or rejected video encoding messages.
     *
     * - Queue type: Classic.
     * - This queue does not need additional arguments as it receives messages directly from
     *   the DLX when the primary queue cannot process them.
     *
     * @return A durable queue for handling dead-lettered video encoding messages.
     */
    @Bean
    public Queue videoEncodingDLXQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_ENCODING_VIDEO_DLX, true, false, false, args);
    }

    /**
     * Configures the primary direct exchange for routing video encoding messages.
     *
     * - Durable: Ensures the exchange persists across broker restarts.
     *
     * @return A DirectExchange for video encoding message routing.
     */
    @Bean
    public DirectExchange encodingExchange() {
        return new DirectExchange(Exchange.X_ENCODING, true, false);
    }

    /**
     * Configures the dead-letter direct exchange for handling failed or expired video encoding messages.
     *
     * - Durable: Ensures the exchange remains active across broker restarts.
     *
     * @return A DirectExchange for dead-letter routing.
     */
    @Bean
    public DirectExchange encodingDLXExchange() {
        return new DirectExchange(Exchange.X_ENCODING_DLX, true, false);
    }

    /**
     * Binds the primary video encoding queue to the primary exchange with a specific routing key.
     *
     * - Routing Key: "video" - Used for precise message routing.
     *
     * @return Binding between the primary queue and the exchange for video encoding tasks.
     */
    @Bean
    public Binding videoBinding() {
        return BindingBuilder.bind(videoEncodingQueue()).to(encodingExchange()).with("video");
    }

    /**
     * Binds the dead-letter queue to the dead-letter exchange with a specific routing key.
     *
     * - Routing Key: "video" - Ensures that dead-lettered messages are routed appropriately.
     *
     * @return Binding between the dead-letter queue and the DLX.
     */
    @Bean
    public Binding videoDLXBinding() {
        return BindingBuilder.bind(videoEncodingDLXQueue()).to(encodingDLXExchange()).with("video");
    }
}
