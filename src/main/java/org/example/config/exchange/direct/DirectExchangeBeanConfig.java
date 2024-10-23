package org.example.config.exchange.direct;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DirectExchangeBeanConfig {

    @Bean
    public Queue eventsBookingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.booking.events", true, false, false, args);
    }

    @Bean
    public Queue journeyBookingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", "classic");
        return new Queue("q.booking.journey", true, false, false, args);
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange("x.booking", true, false);
    }

    @Bean
    public Binding directBindingOne() {
        return BindingBuilder.bind(eventsBookingQueue()).to(bookingExchange()).with("concert.music");
    }

    @Bean
    public Binding directBindingTwo() {
        return BindingBuilder.bind(eventsBookingQueue()).to(bookingExchange()).with("match.ipl");
    }

    @Bean
    public Binding directBindingThree() {
        return BindingBuilder.bind(journeyBookingQueue()).to(bookingExchange()).with("flight");
    }

    @Bean
    public Binding directBindingFour() {
        return BindingBuilder.bind(journeyBookingQueue()).to(bookingExchange()).with("train");
    }
}
