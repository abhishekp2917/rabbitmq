package org.example.controller.stream;

import org.example.model.Employee;
import org.example.service.producer.stream.JsonMessageStreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/stream/message/json")
public class JsonMessageStreamController {

    @Autowired
    private JsonMessageStreamProducer jsonMessageStreamProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishTextMessageToRMQStream(@RequestBody Employee employee) {
        try {
            jsonMessageStreamProducer.publishJsonMessage(employee);
            return new ResponseEntity<>("Employee published to Stream successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Employee couldn't be published to Stream", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
