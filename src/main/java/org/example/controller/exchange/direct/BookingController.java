package org.example.controller.exchange.direct;

import org.example.service.consumer.exchange.direct.EventBookingConsumer;
import org.example.service.consumer.exchange.direct.JourneyBookingConsumer;
import org.example.model.Booking;
import org.example.service.producer.exchange.direct.BookingProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingProducer bookingProducer;

    @Autowired
    private EventBookingConsumer eventBookingConsumer;

    @Autowired
    private JourneyBookingConsumer journeyBookingConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishBookingToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody Booking booking) {
        try {
            bookingProducer.publishBooking(exchangeName, routingKey, booking);
            return new ResponseEntity<>("Booking published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Booking couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/events")
    public ResponseEntity<Booking> consumeEventBookingFromRMQ() {
        try {
            Booking booking = eventBookingConsumer.consumeEventBooking();
            if(booking==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/journey")
    public ResponseEntity<Booking> consumeJourneyBookingFromRMQ() {
        try {
            Booking booking = journeyBookingConsumer.consumeJourneyBooking();
            if(booking==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
