package org.example.config.handlingError.retryMechanism;

import org.example.constants.Exchange;
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

/**
 * Configuration class for setting up RabbitMQ queues, exchanges, and bindings for handling payment processing
 * with retry and dead-letter mechanisms.
 */
@Configuration
public class RetryMechanismBeanConfig {

    /**
     * Defines the main queue for processing credit card payments.
     * This queue routes messages to a 'wait' dead-letter exchange when retries are needed.
     *
     * @return Queue instance for credit card payments.
     */
    @Bean
    public Queue creditCardPaymentQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Ensures the queue uses the 'classic' type for reliable delivery.
        args.put("x-dead-letter-exchange", Exchange.X_PAYMENT_WAIT); // Routes messages to the 'wait' DLX for retry.
        return new Queue(org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD, true, false, false, args);
    }

    /**
     * Defines the 'wait' dead-letter queue for handling delayed re-processing of credit card payment messages.
     * This queue has a TTL (Time-To-Live) and routes messages back to the main exchange after the delay.
     *
     * @return Queue instance for the 'wait' DLX.
     */
    @Bean
    public Queue creditCardPaymentWaitDLXQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Ensures reliability.
        args.put("x-lazy", true); // Optimizes memory usage by storing messages on disk when not in use.
        args.put("x-dead-letter-exchange", Exchange.X_PAYMENT); // Routes messages back to the main payment exchange.
        args.put("x-message-ttl", 10000); // Sets the message TTL to 10 seconds.
        return new Queue(org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_WAIT, true, false, false, args);
    }

    /**
     * Defines the dead-letter queue for messages that have failed after reaching the max retry limit.
     *
     * @return Queue instance for dead-letter messages.
     */
    @Bean
    public Queue creditCardPaymentDeadQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_PAYMENT_CREDIT_CARD_DEAD, true, false, false, args);
    }

    /**
     * Creates the main exchange for processing credit card payments.
     *
     * @return DirectExchange instance for credit card payments.
     */
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(Exchange.X_PAYMENT, true, false);
    }

    /**
     * Creates the 'wait' exchange used for delaying and re-routing messages to the main processing queue.
     *
     * @return DirectExchange instance for 'wait' processing.
     */
    @Bean
    public DirectExchange paymentWaitExchange() {
        return new DirectExchange(Exchange.X_PAYMENT_WAIT, true, false);
    }

    /**
     * Creates the dead-letter exchange for routing messages that have failed after multiple retry attempts.
     *
     * @return DirectExchange instance for dead-letter processing.
     */
    @Bean
    public DirectExchange paymentDeadExchange() {
        return new DirectExchange(Exchange.X_PAYMENT_DEAD, true, false);
    }

    /**
     * Binds the credit card payment queue to the main payment exchange using a specific routing key.
     *
     * @return Binding instance for the main payment queue.
     */
    @Bean
    public Binding creditCardPaymentBinding() {
        return BindingBuilder.bind(creditCardPaymentQueue()).to(paymentExchange()).with("card.credit");
    }

    /**
     * Binds the 'wait' DLX queue to the 'wait' exchange using the same routing key.
     *
     * @return Binding instance for the 'wait' DLX.
     */
    @Bean
    public Binding creditCardPaymentWaitDLXBinding() {
        return BindingBuilder.bind(creditCardPaymentWaitDLXQueue()).to(paymentWaitExchange()).with("card.credit");
    }

    /**
     * Binds the dead-letter queue to the dead-letter exchange for final processing of failed messages.
     *
     * @return Binding instance for the dead-letter queue.
     */
    @Bean
    public Binding creditCardPaymentDeadBinding() {
        return BindingBuilder.bind(creditCardPaymentDeadQueue()).to(paymentDeadExchange()).with("card.credit");
    }

    /**
     * Bean for configuring the DLXErrorHandlingProcessor to manage messages that have exceeded the retry limit.
     *
     * @return DLXErrorHandlingProcessor instance configured for payment dead-letter processing.
     */
    @Bean
    public DLXErrorHandlingProcessor getDLXErrorHandlingProcessor() {
        return new DLXErrorHandlingProcessor(paymentDeadExchange().getName(), 5); // Sets max retry count to 5.
    }
}
