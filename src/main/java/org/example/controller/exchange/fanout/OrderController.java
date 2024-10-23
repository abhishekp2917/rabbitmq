package org.example.controller.exchange.fanout;

import org.example.consumer.exchange.fanout.NotificationConsumer;
import org.example.consumer.exchange.fanout.PaymentConsumer;
import org.example.model.Order;
import org.example.producer.exchange.fanout.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private PaymentConsumer paymentConsumer;

    @Autowired
    private NotificationConsumer notificationConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishOrderToRMQ(@RequestParam String exchangeName, @RequestBody Order order) {
        try {
            orderProducer.publishOrder(exchangeName, order);
            return new ResponseEntity<>("Order published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Order couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/payment")
    public ResponseEntity<Order> consumeOrderPaymentFromRMQ() {
        try {
            Order order = paymentConsumer.consumeOrderPayment();
            if(order==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(order, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/notification")
    public ResponseEntity<Order> consumeOrderNotificationFromRMQ() {
        try {
            Order order = notificationConsumer.consumeOrderNotification();
            if(order==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(order, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
