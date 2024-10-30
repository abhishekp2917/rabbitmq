package org.example.controller.exchange.headers;

import org.example.service.consumer.exchange.headers.EscalatedFNOLClaimConsumer;
import org.example.service.consumer.exchange.headers.NormalFNOLClaimConsumer;
import org.example.model.FNOLClaim;
import org.example.service.producer.exchange.headers.FNOLClaimProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/claim")
public class FNOLClaimController {

    @Autowired
    private FNOLClaimProducer fnolClaimProducer;

    @Autowired
    private NormalFNOLClaimConsumer normalFNOLClaimConsumer;

    @Autowired
    private EscalatedFNOLClaimConsumer escalatedFNOLClaimConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishFNOLClaimToRMQ(@RequestParam String exchangeName, @RequestBody FNOLClaim fnolClaim) {
        try {
            fnolClaimProducer.publishFNOLClaim(exchangeName, fnolClaim);
            return new ResponseEntity<>("FNOL Claim published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("FNOL Claim couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/normal")
    public ResponseEntity<FNOLClaim> consumeNormalFNOLClaimFromRMQ() {
        try {
            FNOLClaim fnolClaim = normalFNOLClaimConsumer.consumeNormalFNOLClaim();
            if(fnolClaim==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(fnolClaim, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/escalated")
    public ResponseEntity<FNOLClaim> consumeEscalatedFNOLClaimFromRMQ() {
        try {
            FNOLClaim fnolClaim = escalatedFNOLClaimConsumer.consumeEscalatedFNOLClaim();
            if(fnolClaim==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(fnolClaim, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
