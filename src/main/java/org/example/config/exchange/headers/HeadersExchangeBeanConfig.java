package org.example.config.exchange.headers;

import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up a Headers exchange with FNOL claim queues.
 *
 * This setup allows message routing based on header attributes rather than routing keys.
 * It's useful for complex routing logic where multiple attributes determine the target queue.
 */
@Configuration
public class HeadersExchangeBeanConfig {

    /**
     * Creates a queue for handling normal FNOL claims.
     *
     * - Queue type: Classic, providing a standard queue setup.
     * - Durable: Set to true for message persistence across broker restarts.
     *
     * @return Queue for normal FNOL claims.
     */
    @Bean
    public Queue normalFNOLClaimQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Classic queue type for regular message processing.
        return new Queue(org.example.constants.Queue.Q_CLAIM_NORMAL, true, false, false, args);
    }

    /**
     * Creates a queue for handling escalated FNOL claims.
     *
     * - Queue type: Classic, for standard functionality.
     * - Durable: Ensures message persistence across broker restarts.
     *
     * @return Queue for escalated FNOL claims.
     */
    @Bean
    public Queue escalatedFNOLClaimQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Classic queue type for escalated claim handling.
        return new Queue(org.example.constants.Queue.Q_CLAIM_ESCALATED, true, false, false, args);
    }

    /**
     * Defines a Headers exchange for routing FNOL claims based on header attributes.
     *
     * - Headers Exchange: This exchange type routes messages based on header values rather than routing keys.
     * - Durable: Ensures the exchange is retained across broker restarts.
     *
     * @return HeadersExchange for claim messages.
     */
    @Bean
    public HeadersExchange claimExchange() {
        return new HeadersExchange(org.example.constants.Exchange.X_CLAIM, true, false);
    }

    /**
     * Binds the `normalFNOLClaimQueue` to the `claimExchange` based on specific headers.
     *
     * - Uses "whereAll" match type, meaning all headers must match for the message to be routed to this queue.
     * - Headers: `livable` = "yes", `fatal` = "no", `damage` = "yes"
     *
     * @return Binding for `normalFNOLClaimQueue` to `claimExchange`.
     */
    @Bean
    public Binding headersBindingOne() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("livable", "yes");
        headers.put("fatal", "no");
        headers.put("damage", "yes"); // All three headers must match for message to be routed here.
        return BindingBuilder.bind(normalFNOLClaimQueue()).to(claimExchange()).whereAll(headers).match();
    }

    /**
     * Binds the `escalatedFNOLClaimQueue` to the `claimExchange` based on specific headers.
     *
     * - Uses "whereAny" match type, meaning at least one of the headers must match for the message to be routed to this queue.
     * - Headers: `livable` = "no" or `fatal` = "yes"
     *
     * @return Binding for `escalatedFNOLClaimQueue` to `claimExchange`.
     */
    @Bean
    public Binding headersBindingTwo() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("livable", "no");
        headers.put("fatal", "yes"); // Either of these headers matching will route message here.
        return BindingBuilder.bind(escalatedFNOLClaimQueue()).to(claimExchange()).whereAny(headers).match();
    }
}
