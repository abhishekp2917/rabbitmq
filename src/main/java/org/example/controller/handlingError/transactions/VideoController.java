package org.example.controller.handlingError.transactions;

import org.example.model.Video;
import org.example.service.producer.handlingError.transactions.VideoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoProducer videoProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishVideosToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody List<Video> videos) {
        try {
            if(videoProducer.publishVideos(exchangeName, routingKey, videos))
                return new ResponseEntity<>("Videos published successfully", HttpStatus.CREATED);
            else
                return new ResponseEntity<>("Videos couldn't be published", HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Videos couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
