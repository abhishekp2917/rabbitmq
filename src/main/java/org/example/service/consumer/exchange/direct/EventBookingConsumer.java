package org.example.service.consumer.exchange.direct;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Booking;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventBookingConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_BOOKING_EVENTS,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_BOOKING_EVENTS,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_BOOKING, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"),
                    key = {"concert.music", "match.ipl"}
            )
    )
    public void listenEventBooking(Booking booking) {
        System.out.println(String.format("Queue : %s, Booking : %s", org.example.constants.Queue.Q_BOOKING_EVENTS, booking));
    }

    public Booking consumeEventBooking() {
        try {
            Booking booking = (Booking)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_BOOKING_EVENTS);
            return booking;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
