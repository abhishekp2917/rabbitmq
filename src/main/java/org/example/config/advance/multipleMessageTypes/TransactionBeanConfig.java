package org.example.config.advance.multipleMessageTypes;

import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionBeanConfig {

    @Bean
    public Queue purchaseTransactionQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_TRANSACTION_PURCHASE, true, false, false, args);
    }

    @Bean
    public TopicExchange purchaseExchange() {
        return new TopicExchange(Exchange.X_TRANSACTION, true, false);
    }

    @Bean
    public Binding purchaseTransactionBinding() {
        return BindingBuilder.bind(purchaseTransactionQueue()).to(purchaseExchange()).with("#.purchase.#");
    }

}
