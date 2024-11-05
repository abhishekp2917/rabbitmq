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

@Configuration
public class BasicStreamBeanConfig {

    @Bean
    public Queue textMessageStream() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.STREAM);
        return new Queue(Stream.S_MESSAGE_TEXT, true, false, false, args);
    }

    @Bean
    public Queue jsonMessageStream() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.STREAM);
        return new Queue(Stream.S_MESSAGE_JSON, true, false, false, args);
    }

    @Bean
    @Qualifier(Stream.S_MESSAGE_TEXT)
    public RabbitStreamTemplate getTextMessageRabbitStreamTemplate(Environment environment) {
        return new RabbitStreamTemplate(environment, Stream.S_MESSAGE_TEXT);
    }

    @Bean
    @Qualifier(Stream.S_MESSAGE_JSON)
    public RabbitStreamTemplate getJsonMessageRabbitStreamTemplate(Environment environment, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        var rabbitStreamTemplate = new RabbitStreamTemplate(environment, Stream.S_MESSAGE_JSON);
        rabbitStreamTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitStreamTemplate;
    }
}
