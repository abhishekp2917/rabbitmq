package org.example.config.basic;

import org.example.constants.QueueType;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PriorityQueueBeanConfig {

    @Bean
    public Queue PriorityMessageQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        args.put("x-max-priority", 255);
        return new Queue("q.priority.message", true, false, false, args);
    }
}
