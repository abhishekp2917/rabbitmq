package org.example.config.basic;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BasicQueueBeanConfig {

    @Bean
    public Queue textMessageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.message.text", true, false, false, args);
    }

    @Bean
    public Queue jsonMessageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.message.json", true, false, false, args);
    }
}
