package org.example.controller.handlingError.retryMechanism;

import org.example.model.Payment;
import org.example.service.producer.handlingError.retryMechanism.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentProducer paymentProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishPaymentToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody Payment payment) {
        try {
            paymentProducer.publishPayment(exchangeName, routingKey, payment);
            return new ResponseEntity<>("Payment published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Payment couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
