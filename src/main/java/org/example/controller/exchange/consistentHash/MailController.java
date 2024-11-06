package org.example.controller.exchange.consistentHash;

import org.example.model.Mail;
import org.example.service.producer.exchange.consistentHash.MailProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailProducer mailProducer;
    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMailToRMQ(@RequestBody List<Mail> mails) {
        try {
            mailProducer.publishMails(mails);
            return new ResponseEntity<>("Mails published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Mails couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
