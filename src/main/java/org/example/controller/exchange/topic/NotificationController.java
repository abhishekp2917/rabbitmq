package org.example.controller.exchange.topic;

import org.example.service.consumer.exchange.topic.OtpNotificationConsumer;
import org.example.service.consumer.exchange.topic.PaymentNotificationConsumer;
import org.example.service.consumer.exchange.topic.SubscriberNotificationConsumer;
import org.example.model.Notification;
import org.example.service.producer.exchange.topic.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private PaymentNotificationConsumer paymentNotificationConsumer;

    @Autowired
    private OtpNotificationConsumer otpNotificationConsumer;

    @Autowired
    private SubscriberNotificationConsumer subscriberNotificationConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishNotificationToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody Notification notification) {
        try {
            notificationProducer.publishNotification(exchangeName, routingKey, notification);
            return new ResponseEntity<>("Notification published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Notification couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/payment")
    public ResponseEntity<Notification> consumePaymentNotificationFromRMQ() {
        try {
            Notification notification = paymentNotificationConsumer.consumePaymentNotification();
            if(notification==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(notification, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/otp")
    public ResponseEntity<Notification> consumeOtpNotificationFromRMQ() {
        try {
            Notification notification = otpNotificationConsumer.consumeOtpNotification();
            if(notification==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(notification, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/subscriber")
    public ResponseEntity<Notification> consumeSubscriberNotificationFromRMQ() {
        try {
            Notification notification = subscriberNotificationConsumer.consumeSubscriberNotification();
            if(notification==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(notification, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
