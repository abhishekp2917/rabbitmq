package org.example.controller.stream;

import org.example.service.producer.stream.TextMessageStreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/stream/message/text")
public class TextMessageStreamController {

    @Autowired
    private TextMessageStreamProducer textMessageStreamProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishTextMessageToRMQStream(@RequestParam String message) {
        try {
            textMessageStreamProducer.publishTextMessage(message);
            return new ResponseEntity<>("Message published to Stream successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Message couldn't be published to Stream", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
