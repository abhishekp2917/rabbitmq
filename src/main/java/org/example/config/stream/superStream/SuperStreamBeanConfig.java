package org.example.config.stream.superStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import org.example.constants.Consumer;
import org.example.constants.Stream;
import org.example.model.Payment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.SuperStream;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

@Configuration
public class SuperStreamBeanConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SuperStream paymentNotificationSuperStream() {
        return new SuperStream(Stream.S_PAYMENT, 3);
    }

    @Bean
    @Qualifier(Stream.S_PAYMENT)
    public RabbitStreamTemplate getpaymentNotificationRabbitStreamTemplate(Environment environment, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        var rabbitStreamTemplate = new RabbitStreamTemplate(environment, Stream.S_PAYMENT);
        rabbitStreamTemplate.setMessageConverter(jackson2JsonMessageConverter);
        rabbitStreamTemplate.setSuperStreamRouting(message -> {
            try {
                Payment payment = objectMapper.readValue(message.getBodyAsBinary(), Payment.class);
                return payment.getPaymentMethod();
            }
            catch (Exception ex) {
                return "Other";
            }
        });
        return rabbitStreamTemplate;
    }

    @Bean
    public StreamListenerContainer paymentSuperStreamListenerContainer(Environment environment) {
        var container = new StreamListenerContainer(environment);
        container.superStream(Stream.S_PAYMENT, Consumer.CG_PAYMENT_SUPER_STREAM_1);
        container.setConsumerCustomizer((id, builder) -> {
            builder.
                    offset(OffsetSpecification.next())
                    .autoTrackingStrategy();
        });
        container.setupMessageListener(message -> {
            try {
                Thread.sleep(2000);
                Payment payment = objectMapper.readValue(message.getBody(), Payment.class);
                System.out.println(String.format("Super Stream : %s, Payment : %s", Stream.S_PAYMENT, payment));
            } catch (Exception ex) {
                System.out.println(String.format("Error occurred : %s", ex.getMessage()));
            }
        });
        return container;
    }
}
