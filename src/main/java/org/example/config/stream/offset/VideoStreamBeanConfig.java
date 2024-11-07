package org.example.config.stream.offset;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import org.example.constants.Consumer;
import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.example.constants.Stream;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * This configuration class sets up queues, exchanges, and listeners for handling video encoding
 * tasks using RabbitMQ Streams. It configures the offset strategy for each consumer, which defines
 * where in the stream to start consuming messages.
 *
 * RabbitMQ Streams allow for more complex messaging patterns, where consumers can read from specific
 * offsets, including the first message, the last message, or any custom offset.
 * This configuration supports those use cases for video encoding tasks with different offset requirements.
 */
@Configuration
public class VideoStreamBeanConfig {

    /**
     * Creates a queue for video encoding tasks using RabbitMQ Stream.
     *
     * The queue is configured to store messages persistently with stream characteristics.
     * The queue is bound to a stream, and messages will be processed by consumers.
     *
     * @return the configured Queue for video encoding stream.
     */
    @Bean
    public Queue videoEncodingStream() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.STREAM); // Specifies that this is a stream queue
        return new Queue(Stream.S_VIDEO_ENCODING, true, false, false, args);
    }

    /**
     * Creates a fanout exchange for video streams.
     *
     * A fanout exchange broadcasts messages to all bound queues. This exchange is used to send
     * video encoding tasks to all consumers listening on this exchange.
     *
     * @return the configured FanoutExchange instance for video streams.
     */
    @Bean
    public FanoutExchange videoStreamExchange() {
        return new FanoutExchange(Exchange.X_STREAM_VIDEO, true, false);
    }

    /**
     * Binds the video encoding stream queue to the video stream exchange.
     *
     * Binding ensures that messages published to the exchange will be routed to the queue
     * specified by this binding. Here, the video encoding stream queue is bound to the
     * fanout exchange.
     *
     * @return the Binding instance that connects the queue and exchange.
     */
    @Bean
    public Binding videoEncodingStreamBinding() {
        return BindingBuilder.bind(videoEncodingStream()).to(videoStreamExchange());
    }

    /**
     * Configures a consumer factory to start consuming messages from a specific absolute offset.
     * In this case, the consumer starts reading from offset 4 in the stream.
     *
     * A RabbitListenerContainerFactory is used to configure and create a RabbitListenerContainer,
     * which is responsible for consuming messages from a RabbitMQ queue or stream.
     *
     * By defining a RabbitListenerContainerFactory as a Spring bean, you can easily customize the way consumers
     * (listeners) are created and how they interact with RabbitMQ.
     *
     * This configuration is useful when you want to consume messages starting from a known point in
     * the stream, for example, resuming from a previous state or a checkpoint.
     *
     * @param environment the RabbitMQ stream environment.
     * @return the configured RabbitListenerContainerFactory instance.
     */
    @Bean(name = Consumer.C_STREAM_OFFSET_ABSOLUTE_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getAbsoluteOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true); // Use RabbitMQ Stream as the native listener
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING) // Specifies the stream to consume from
                    .name(Consumer.C_STREAM_OFFSET_ABSOLUTE_1) // Assign a name to the consumer
                    .offset(OffsetSpecification.offset(4)) // Start consuming from offset 4
                    .autoTrackingStrategy(); // Enable automatic offset tracking
        });
        return factory;
    }

    /**
     * Configures a consumer factory to start consuming messages from the first message in the stream.
     *
     * This configuration is useful when you want the consumer to start reading messages from
     * the beginning of the stream. It ensures the consumer processes all messages in order from
     * the start.
     *
     * @param environment the RabbitMQ stream environment.
     * @return the configured RabbitListenerContainerFactory instance.
     */
    @Bean(name = Consumer.C_STREAM_OFFSET_FIRST_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getFirstOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_FIRST_1)
                    .offset(OffsetSpecification.first()) // Start from the first message in the stream
                    .autoTrackingStrategy();
        });
        return factory;
    }

    /**
     * Configures a consumer factory to start consuming messages from the last message in the stream.
     *
     * This configuration is useful for consumers that only want to process the most recent messages
     * in the stream, skipping over earlier ones that may have already been processed.
     *
     * @param environment the RabbitMQ stream environment.
     * @return the configured RabbitListenerContainerFactory instance.
     */
    @Bean(name = Consumer.C_STREAM_OFFSET_LAST_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getLastOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_LAST_1)
                    .offset(OffsetSpecification.last()) // Start from the last message in the stream
                    .autoTrackingStrategy();
        });
        return factory;
    }

    /**
     * Configures a consumer factory to start consuming messages from the next offset after the last
     * acknowledged message.
     *
     * This configuration is useful when the consumer needs to resume from where it last left off,
     * ensuring that no messages are processed more than once.
     *
     * @param environment the RabbitMQ stream environment.
     * @return the configured RabbitListenerContainerFactory instance.
     */
    @Bean(name = Consumer.C_STREAM_OFFSET_NEXT_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getNextOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_NEXT_1)
                    .offset(OffsetSpecification.next()) // Start from the next offset after the last acknowledged message
                    .autoTrackingStrategy();
        });
        return factory;
    }

    /**
     * Configures a consumer factory to start consuming messages from a timestamp offset.
     * In this case, the offset is set to 5 minutes ago (to allow for message processing from that time).
     *
     * This configuration is useful when you want the consumer to process messages from a specific
     * point in time, for example, recovering from a downtime.
     *
     * @param environment the RabbitMQ stream environment.
     * @return the configured RabbitListenerContainerFactory instance.
     */
    @Bean(name = Consumer.C_STREAM_OFFSET_TIMESTAMP_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getTimestampOffsetRabbitListenerContainerFactory(Environment environment) {
        // Calculate the timestamp offset (5 minutes ago in milliseconds)
        var timestampOffsetInMillis = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(5).toEpochSecond() * 1000;

        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_TIMESTAMP_1)
                    .offset(OffsetSpecification.timestamp(timestampOffsetInMillis)) // Start from 5 minutes ago
                    .autoTrackingStrategy();
        });
        return factory;
    }
}
