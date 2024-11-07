package org.example.config.exchange.direct;

import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up a direct exchange with specific routing keys.
 *
 * This setup supports routing messages directly to queues based on an exact match of routing keys.
 * It defines a direct exchange for booking-related messages, with separate queues for event bookings and journey bookings.
 */
@Configuration
public class DirectExchangeBeanConfig {

    /**
     * Defines a queue for booking event-related messages.
     *
     * - Classic queue type: Configured as "classic" to support standard message handling.
     * - Durable: This queue will persist even if the broker restarts.
     *
     * @return Queue object for event bookings.
     */
    @Bean
    public Queue eventsBookingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Standard classic queue type
        return new Queue(org.example.constants.Queue.Q_BOOKING_EVENTS, true, false, false, args);
    }

    /**
     * Defines a queue for booking journey-related messages.
     *
     * - Classic queue type and durable for persistence through broker restarts.
     *
     * @return Queue object for journey bookings.
     */
    @Bean
    public Queue journeyBookingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC); // Standard classic queue type
        return new Queue(org.example.constants.Queue.Q_BOOKING_JOURNEY, true, false, false, args);
    }

    /**
     * Defines a direct exchange for booking messages.
     *
     * - A direct exchange routes messages to queues based on an exact match of the routing key.
     * - Durable, ensuring persistence even if the broker restarts.
     *
     * @return DirectExchange object for booking-related routing.
     */
    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(org.example.constants.Exchange.X_BOOKING, true, false);
    }

    /**
     * Binds `eventsBookingQueue` to `bookingExchange` for the "concert.music" routing key.
     *
     * - Messages with a routing key of "concert.music" will be directed to `eventsBookingQueue`.
     *
     * @return Binding for "concert.music" to `eventsBookingQueue`.
     */
    @Bean
    public Binding directBindingOne() {
        return BindingBuilder.bind(eventsBookingQueue()).to(bookingExchange()).with("concert.music");
    }

    /**
     * Binds `eventsBookingQueue` to `bookingExchange` for the "match.ipl" routing key.
     *
     * - Messages with a routing key of "match.ipl" will also be routed to `eventsBookingQueue`.
     *
     * @return Binding for "match.ipl" to `eventsBookingQueue`.
     */
    @Bean
    public Binding directBindingTwo() {
        return BindingBuilder.bind(eventsBookingQueue()).to(bookingExchange()).with("match.ipl");
    }

    /**
     * Binds `journeyBookingQueue` to `bookingExchange` for the "flight" routing key.
     *
     * - Messages with a routing key of "flight" will be routed to `journeyBookingQueue`.
     *
     * @return Binding for "flight" to `journeyBookingQueue`.
     */
    @Bean
    public Binding directBindingThree() {
        return BindingBuilder.bind(journeyBookingQueue()).to(bookingExchange()).with("flight");
    }

    /**
     * Binds `journeyBookingQueue` to `bookingExchange` for the "train" routing key.
     *
     * - Messages with a routing key of "train" will be routed to `journeyBookingQueue`.
     *
     * @return Binding for "train" to `journeyBookingQueue`.
     */
    @Bean
    public Binding directBindingFour() {
        return BindingBuilder.bind(journeyBookingQueue()).to(bookingExchange()).with("train");
    }
}
