package org.example.config.exchange.topic;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TopicExchangeBeanConfig {

    @Bean
    public Queue otpNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.notification.otp", true, false, false, args);
    }

    @Bean
    public Queue paymentNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.notification.payment", true, false, false, args);
    }

    @Bean                                                                                                  
    public Queue subscriberNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.notification.subscriber", true, false, false, args);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange("x.notification", true, false);
    }

    @Bean
    public Binding topicBindingOne() {
        return BindingBuilder.bind(otpNotificationQueue()).to(notificationExchange()).with("#.otp.#");
    }

    @Bean
    public Binding topicBindingTwo() {
        return BindingBuilder.bind(paymentNotificationQueue()).to(notificationExchange()).with("*.payment.*");
    }

    @Bean
    public Binding topicBindingThree() {
        return BindingBuilder.bind(subscriberNotificationQueue()).to(notificationExchange()).with("subscriber.#");
    }
}
