package org.example.config.handlingError.transactions;

import org.example.constants.QueueType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionsBeanConfig {

    @Bean
    public Queue video720pEncodingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_VIDEO_ENCODING_720P, true, false, false, args);
    }

    @Bean
    public DirectExchange videoExchange() {
        return new DirectExchange("x.video", true, false);
    }


    @Bean
    public Binding video720pEncodingBinding() {
        return BindingBuilder.bind(video720pEncodingQueue()).to(videoExchange()).with("video.encoding");
    }
}
