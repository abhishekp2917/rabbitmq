package org.example.consumer.exchange.topic;

import org.example.model.Booking;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventBookingConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.booking.events", ackMode = "NONE", concurrency = "3-5")
    public void listenEventBooking(Booking booking) {
        System.out.println(String.format("Queue : q.booking.events, Booking : %s", booking));
    }

    public Booking consumeEventBooking() {
        try {
            Booking booking = (Booking)rabbitTemplate.receiveAndConvert("q.booking.events");
            return booking;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
