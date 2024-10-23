package org.example.consumer.exchange.topic;

import org.example.model.Booking;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyBookingConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.booking.journey", ackMode = "NONE", concurrency = "3-5")
    public void listenJourneyBooking(Booking booking) {
        System.out.println(String.format("Queue : q.booking.journey, Booking : %s", booking));
    }

    public Booking consumeJourneyBooking() {
        try {
            Booking booking = (Booking)rabbitTemplate.receiveAndConvert("q.booking.journey");
            return booking;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
