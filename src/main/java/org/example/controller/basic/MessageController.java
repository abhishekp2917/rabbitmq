package org.example.controller.basic;

import org.example.consumer.basic.TextMessageConsumer;
import org.example.producer.basic.TextMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private TextMessageProducer textMessageProducer;

    @Autowired
    private TextMessageConsumer textMessageConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishTextMessageToRMQ(@RequestParam String queueName, @RequestParam String message) {
        try {
            textMessageProducer.publishMessage(queueName, message);
            return new ResponseEntity<>("Message published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Message couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume")
    public ResponseEntity<String> consumeTextMessageFromRMQ(@RequestParam String queueName) {
        try {
            String message = textMessageConsumer.consumeTextMessage(queueName);
            if(message.length()==0) return new ResponseEntity<>("Queue is empty", HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(String.format("Queue : %s, Message : %s", queueName, message), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Message couldn't be consumed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
