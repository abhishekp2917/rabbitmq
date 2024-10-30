package org.example.config.handlingError.deadLetterExchange;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DeadLetterExchangeBeanConfig {

    @Bean
    public Queue videoEncodingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        args.put("x-dead-letter-exchange", "x.encoding.dlx");
        args.put("x-message-ttl", 10000);
        return new Queue("q.encoding.video", true, false, false, args);
    }

    @Bean
    public Queue videoEncodingDLXQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.encoding.video.dlx", true, false, false, args);
    }

    @Bean
    public DirectExchange encodingExchange() {
        return new DirectExchange("x.encoding", true, false);
    }

    @Bean
    public DirectExchange encodingDLXExchange() {
        return new DirectExchange("x.encoding.dlx", true, false);
    }

    @Bean
    public Binding videoBinding() {
        return BindingBuilder.bind(videoEncodingQueue()).to(encodingExchange()).with("video");
    }

    @Bean
    public Binding videoDLXBinding() {
        return BindingBuilder.bind(videoEncodingDLXQueue()).to(encodingDLXExchange()).with("video");
    }
}
