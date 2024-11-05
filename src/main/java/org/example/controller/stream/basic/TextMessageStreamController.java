package org.example.controller.stream.basic;

import org.example.service.producer.stream.basic.TextMessageStreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<String> publishTextMessageToRMQStream(@RequestBody List<String> messages) {
        try {
            textMessageStreamProducer.publishTextMessage(messages);
            return new ResponseEntity<>("Messages published to Stream successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Messages couldn't be published to Stream", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
