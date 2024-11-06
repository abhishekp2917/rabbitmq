package org.example.config.exchange.consistentHash;

import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsistentHashExchangeBeanConfig {

    @Bean
    public Queue mailQueue1() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MAIL_1, true, false, false, args);
    }

    @Bean
    public Queue mailQueue2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MAIL_2, true, false, false, args);
    }

    @Bean
    public Exchange mailExchange() {
        return new CustomExchange(org.example.constants.Exchange.X_MAIL, ExchangeType.X_CONSISTENT_HASH, true, false);
    }

    @Bean
    public Binding mailBindingOne() {
        return BindingBuilder.bind(mailQueue1()).to(mailExchange()).with("3").noargs();
    }

    @Bean
    public Binding mailBindingTwo() {
        return BindingBuilder.bind(mailQueue2()).to(mailExchange()).with("2").noargs();
    }
}
