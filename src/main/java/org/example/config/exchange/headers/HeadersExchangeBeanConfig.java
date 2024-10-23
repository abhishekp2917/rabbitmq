package org.example.config.exchange.headers;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HeadersExchangeBeanConfig {

    @Bean
    public Queue normalFNOLClaimQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.claim.normal", true, false, false, args);
    }

    @Bean
    public Queue escalatedFNOLClaimQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.claim.escalated", true, false, false, args);
    }

    @Bean
    public HeadersExchange claimExchange() {
        return new HeadersExchange("x.claim", true, false);
    }

    @Bean
    public Binding headersBindingOne() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("livable", "yes");
        headers.put("fatal", "no");
        headers.put("damage", "yes");
        return BindingBuilder.bind(normalFNOLClaimQueue()).to(claimExchange()).whereAll(headers).match();
    }

    @Bean
    public Binding headersBindingTwo() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("livable", "no");
        headers.put("fatal", "yes");
        return BindingBuilder.bind(escalatedFNOLClaimQueue()).to(claimExchange()).whereAny(headers).match();
    }
}
