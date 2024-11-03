package org.example.config.advance.prefetch;

import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PrefetchBeanConfig {

    @Bean
    public Queue audioEncodingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_AUDIO_ENCODING, true, false, false, args);
    }

    @Bean
    public Queue audioCompressionQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_AUDIO_COMPRESSION, true, false, false, args);
    }

    @Bean
    public FanoutExchange audioExchange() {
        return new FanoutExchange(Exchange.X_AUDIO, true, false);
    }

    @Bean
    public Binding audioEncodingBinding() {
        return BindingBuilder.bind(audioEncodingQueue()).to(audioExchange());
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> getAudioCompressionQueueContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory
    ) {
        var factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPrefetchCount(10);
        return factory;
    }
}
