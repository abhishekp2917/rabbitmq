package org.example.controller.advance.scheduling;

import org.example.model.Media;
import org.example.service.producer.advance.scheduling.MediaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaProducer mediaProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMediaToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody List<Media> medias) {
        try {
            mediaProducer.publishMedias(exchangeName, routingKey, medias);
            return new ResponseEntity<>("All the Medias published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Some or all the Medias couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
