package org.example.controller.stream.superStream;

import org.example.model.Payment;
import org.example.service.producer.stream.superStream.PaymentSuperStreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/stream/super/payment/")
public class PaymentNotificationSuperStreamController {

    @Autowired
    private PaymentSuperStreamProducer paymentSuperStreamProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishPaymentToRMQSuperStream(@RequestBody List<Payment> payments) {
        try {
            paymentSuperStreamProducer.publishPayment(payments);
            return new ResponseEntity<>("Payment published to Super Stream successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Payment couldn't be published to Super Stream", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
