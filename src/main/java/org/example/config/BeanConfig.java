package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

//    @Bean
//    public RabbitTemplate getRabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
//            if (ack) {
//                System.out.println("Message acknowledged by broker with ID: " + ((correlationData!=null)?correlationData.getId():-1));
//            }
//            else {
//                System.err.println("Message NOT acknowledged by broker with ID: " + ((correlationData!=null)?correlationData.getId():-1) + ". Cause: " + cause);
//            }
//        });
//        return rabbitTemplate;
//    }

    @Bean
    public Jackson2JsonMessageConverter getJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
