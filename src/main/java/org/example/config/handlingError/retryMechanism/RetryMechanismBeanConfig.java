package org.example.config.handlingError.retryMechanism;

import org.example.constants.QueueType;
import org.example.util.handlingError.retryMechanism.DLXErrorHandlingProcessor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RetryMechanismBeanConfig {

    @Bean
    public Queue creditCardPaymentQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        args.put("x-dead-letter-exchange", "x.payment.wait");
        return new Queue("q.payment.creditCard", true, false, false, args);
    }

    @Bean
    public Queue creditCardPaymentWaitDLXQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        args.put("x-lazy", true);
        args.put("x-dead-letter-exchange", "x.payment");
        args.put("x-message-ttl", 10000);
        return new Queue("q.payment.creditCard.wait", true, false, false, args);
    }

    @Bean
    public Queue creditCardPaymentDeadQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue("q.payment.creditCard.dead", true, false, false, args);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("x.payment", true, false);
    }

    @Bean
    public DirectExchange paymentWaitExchange() {
        return new DirectExchange("x.payment.wait", true, false);
    }

    @Bean
    public DirectExchange paymentDeadExchange() {
        return new DirectExchange("x.payment.dead", true, false);
    }

    @Bean
    public Binding creditCardPaymentBinding() {
        return BindingBuilder.bind(creditCardPaymentQueue()).to(paymentExchange()).with("card.credit");
    }

    @Bean
    public Binding creditCardPaymentWaitDLXBinding() {
        return BindingBuilder.bind(creditCardPaymentWaitDLXQueue()).to(paymentWaitExchange()).with("card.credit");
    }

    @Bean
    public Binding creditCardPaymentDeadBinding() {
        return BindingBuilder.bind(creditCardPaymentDeadQueue()).to(paymentDeadExchange()).with("card.credit");
    }

    @Bean
    public DLXErrorHandlingProcessor getDLXErrorHandlingProcessor() {
        return new DLXErrorHandlingProcessor(paymentDeadExchange().getName(), 5);
    }
}
