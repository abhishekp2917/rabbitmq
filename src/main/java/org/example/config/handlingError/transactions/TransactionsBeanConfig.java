package org.example.config.handlingError.transactions;

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
 * Configuration class for setting up RabbitMQ components related to video encoding processing.
 */
@Configuration
public class TransactionsBeanConfig {

    /**
     * Defines a queue for handling 720p video encoding tasks.
     * The queue is configured with a type property to ensure it operates in 'classic' mode for reliability.
     *
     * @return Queue instance for 720p video encoding.
     */
    @Bean
    public Queue video720pEncodingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Ensures the queue uses the 'classic' type for message durability.
        return new Queue(org.example.constants.Queue.Q_VIDEO_ENCODING_720P, true, false, false, args);
    }

    /**
     * Creates a direct exchange for routing video encoding messages.
     * This exchange routes messages based on a routing key.
     *
     * @return DirectExchange instance for video processing.
     */
    @Bean
    public DirectExchange videoExchange() {
        return new DirectExchange(Exchange.X_VIDEO, true, false); // Durable and non-auto-deletable exchange.
    }

    /**
     * Binds the 720p video encoding queue to the video exchange using a specific routing key.
     * This ensures that messages with the routing key "video.encoding" are directed to the 720p queue.
     *
     * @return Binding instance between the video exchange and the 720p encoding queue.
     */
    @Bean
    public Binding video720pEncodingBinding() {
        return BindingBuilder.bind(video720pEncodingQueue()).to(videoExchange()).with("video.encoding");
    }
}
