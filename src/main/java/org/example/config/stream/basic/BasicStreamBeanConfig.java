package org.example.config.stream.basic;

import com.rabbitmq.stream.Environment;
import org.example.constants.QueueType;
import org.example.constants.Stream;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up RabbitMQ stream queues and stream templates.
 * This configures both text-based and JSON-based message streams.
 */
@Configuration
public class BasicStreamBeanConfig {

    /**
     * Defines a RabbitMQ stream queue for text messages.
     * This queue is configured to use the 'stream' type, which provides high-throughput and persistent streaming.
     *
     * @return a Queue instance configured for text messages.
     */
    @Bean
    public Queue textMessageStream() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.STREAM); // Indicates the queue type as a stream for continuous processing.
        return new Queue(Stream.S_MESSAGE_TEXT, true, false, false, args);
    }

    /**
     * Defines a RabbitMQ stream queue for JSON messages.
     * This queue is configured to use the 'stream' type for handling structured JSON payloads.
     *
     * @return a Queue instance configured for JSON messages.
     */
    @Bean
    public Queue jsonMessageStream() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.STREAM); // Indicates the queue type as a stream.
        return new Queue(Stream.S_MESSAGE_JSON, true, false, false, args);
    }

    /**
     * Creates a RabbitStreamTemplate for sending text-based messages.
     * This template is used to produce messages to the specified stream.
     * Each template is tailored to work with the respective queue.
     *
     * @param environment the RabbitMQ streaming environment.
     * @return a RabbitStreamTemplate instance configured for text message streaming.
     */
    @Bean
    @Qualifier(Stream.S_MESSAGE_TEXT)
    public RabbitStreamTemplate getTextMessageRabbitStreamTemplate(Environment environment) {
        return new RabbitStreamTemplate(environment, Stream.S_MESSAGE_TEXT);
    }

    /**
     * Creates a RabbitStreamTemplate for sending JSON-based messages.
     * The template uses a Jackson2JsonMessageConverter for converting Java objects to JSON.
     *
     * @param environment the RabbitMQ streaming environment.
     * @param jackson2JsonMessageConverter the converter for JSON message serialization.
     * @return a RabbitStreamTemplate instance configured for JSON message streaming.
     */
    @Bean
    @Qualifier(Stream.S_MESSAGE_JSON)
    public RabbitStreamTemplate getJsonMessageRabbitStreamTemplate(Environment environment, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        var rabbitStreamTemplate = new RabbitStreamTemplate(environment, Stream.S_MESSAGE_JSON);
        rabbitStreamTemplate.setMessageConverter(jackson2JsonMessageConverter); // Ensures messages are sent in JSON format.
        return rabbitStreamTemplate;
    }
}
