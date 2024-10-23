package org.example.config.exchange.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FanOutExchangeBeanConfig {

    @Bean
    public Queue orderNotificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.order.notification", true, false, false, args);
    }

    @Bean
    public Queue orderPaymentQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.order.payment", true, false, false, args);
    }

    @Bean
    public FanoutExchange orderExchange() {
        return new FanoutExchange("x.order", true, false);
    }

    @Bean
    public Binding fanOutBindingOne() {
        return BindingBuilder.bind(orderNotificationQueue()).to(orderExchange());
    }

    @Bean
    public Binding fanOutBindingTwo() {
        return BindingBuilder.bind(orderPaymentQueue()).to(orderExchange());
    }
}
