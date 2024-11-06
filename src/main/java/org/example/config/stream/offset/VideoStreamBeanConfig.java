package org.example.config.stream.offset;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import org.example.constants.Consumer;
import org.example.constants.Exchange;
import org.example.constants.QueueType;
import org.example.constants.Stream;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class VideoStreamBeanConfig {

    @Bean
    public Queue videoEncodingStream() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.STREAM);
        return new Queue(Stream.S_VIDEO_ENCODING, true, false, false, args);
    }

    @Bean
    public FanoutExchange videoStreamExchange() {
        return new FanoutExchange(Exchange.X_STREAM_VIDEO, true, false);
    }

    @Bean
    public Binding videoEncodingStreamBinding() {
        return BindingBuilder.bind(videoEncodingStream()).to(videoStreamExchange());
    }

    @Bean(name = Consumer.C_STREAM_OFFSET_ABSOLUTE_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getAbsoluteOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_ABSOLUTE_1)
                    .offset(OffsetSpecification.offset(4))
                    .autoTrackingStrategy();
        });
        return factory;
    }

    @Bean(name = Consumer.C_STREAM_OFFSET_FIRST_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getFirstOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_FIRST_1)
                    .offset(OffsetSpecification.first())
                    .autoTrackingStrategy();
        });
        return factory;
    }

    @Bean(name = Consumer.C_STREAM_OFFSET_LAST_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getLastOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_LAST_1)
                    .offset(OffsetSpecification.last())
                    .autoTrackingStrategy();
        });
        return factory;
    }

    @Bean(name = Consumer.C_STREAM_OFFSET_NEXT_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getNextOffsetRabbitListenerContainerFactory(Environment environment) {
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_NEXT_1)
                    .offset(OffsetSpecification.next())
                    .autoTrackingStrategy();
        });
        return factory;
    }

    @Bean(name = Consumer.C_STREAM_OFFSET_TIMESTAMP_1)
    public RabbitListenerContainerFactory<StreamListenerContainer> getTimestampOffsetRabbitListenerContainerFactory(Environment environment) {
        var timestampOffsetInMillis = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(5).toEpochSecond()*1000;
        var factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(true);
        factory.setConsumerCustomizer((id, builder) -> {
            builder.stream(Stream.S_VIDEO_ENCODING)
                    .name(Consumer.C_STREAM_OFFSET_TIMESTAMP_1)
                    .offset(OffsetSpecification.timestamp(timestampOffsetInMillis))
                    .autoTrackingStrategy();
        });
        return factory;
    }
}
