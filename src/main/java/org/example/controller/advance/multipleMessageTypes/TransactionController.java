package org.example.controller.advance.multipleMessageTypes;

import org.example.service.producer.advance.multipleMessageTypes.TransactionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionProducer transactionProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMediaToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody List<Object> transactions) {
        try {
            transactionProducer.publishTransaction(exchangeName, routingKey, transactions);
            return new ResponseEntity<>("All the Transactions published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Some or all the Transactions couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
