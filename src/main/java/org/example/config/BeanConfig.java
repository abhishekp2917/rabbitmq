package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining essential beans used in the application,
 * including message conversion and JSON handling.
 */
@Configuration
public class BeanConfig {

    /**
     * Creates a bean for Jackson-based message conversion.
     * This ensures that RabbitMQ messages are converted to and from JSON format,
     * simplifying the serialization and deserialization of message payloads.
     *
     * @return Jackson2JsonMessageConverter instance for message conversion.
     */
    @Bean
    public Jackson2JsonMessageConverter getJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a bean for Jackson's ObjectMapper, which is used for
     * custom JSON serialization and deserialization logic throughout the application.
     *
     * @return ObjectMapper instance for handling JSON operations.
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
