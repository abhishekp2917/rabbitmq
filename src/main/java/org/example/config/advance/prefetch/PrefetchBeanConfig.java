package org.example.config.advance.prefetch;

import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up queues, exchanges, and listener container factory
 * with a specific prefetch count for RabbitMQ audio processing tasks.
 *
 * This configuration handles:
 * - Queue and exchange definitions for audio encoding and compression.
 * - Fanout exchange setup for broadcasting messages to multiple queues.
 * - Listener container factory with prefetch configuration to optimize message consumption.
 */
@Configuration
public class PrefetchBeanConfig {

    /**
     * Defines the queue for audio encoding tasks.
     *
     * This queue is configured as:
     * - Durable: Persistent across broker restarts.
     * - Classic queue type: Specified with `x-queue-type` to maintain compatibility with the default type in RabbitMQ.
     *
     * @return Queue object for audio encoding.
     */
    @Bean
    public Queue audioEncodingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_AUDIO_ENCODING, true, false, false, args);
    }

    /**
     * Defines the queue for audio compression tasks.
     *
     * Like the encoding queue, this queue is durable and set as a classic type queue.
     *
     * @return Queue object for audio compression.
     */
    @Bean
    public Queue audioCompressionQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_AUDIO_COMPRESSION, true, false, false, args);
    }

    /**
     * Defines a fanout exchange for audio tasks.
     *
     * A fanout exchange broadcasts all messages to all bound queues, which is ideal for
     * scenarios where the same message needs to be processed by multiple consumers.
     *
     * @return FanoutExchange object for audio processing.
     */
    @Bean
    public FanoutExchange audioExchange() {
        return new FanoutExchange(Exchange.X_AUDIO, true, false);
    }

    /**
     * Binds the audio encoding queue to the audio fanout exchange.
     *
     * This binding ensures that messages published to the audio exchange are routed
     * to the audio encoding queue.
     *
     * @return Binding object linking the encoding queue to the fanout exchange.
     */
    @Bean
    public Binding audioEncodingBinding() {
        return BindingBuilder.bind(audioEncodingQueue()).to(audioExchange());
    }

    /**
     * Configures a listener container factory with a prefetch count for the audio compression queue.
     *
     * The prefetch count controls the number of messages that a consumer can prefetch from the broker,
     * enhancing message processing efficiency by allowing up to 10 unacknowledged messages to be held at a time.
     *
     * This setting is useful for controlling load and optimizing consumer performance for high-throughput tasks.
     *
     * @param configurer Spring Boot's RabbitMQ container factory configurer for general settings.
     * @param connectionFactory RabbitMQ connection factory.
     * @return RabbitListenerContainerFactory configured with a specific prefetch count.
     */
    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> getAudioCompressionQueueContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory
    ) {
        var factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPrefetchCount(10); // Limits the consumer to 10 unacknowledged messages at a time
        return factory;
    }
}
