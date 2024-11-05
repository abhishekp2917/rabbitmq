package org.example.controller.stream.offset;

import org.example.model.Video;
import org.example.service.producer.stream.offset.VideoStreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/stream/video")
public class VideoStreamController {

    @Autowired
    private VideoStreamProducer videoStreamProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishVideoToRMQStream(@RequestBody List<Video> videos) {
        try {
            videoStreamProducer.publishVideos(videos);
            return new ResponseEntity<>("Video published to Stream successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Video couldn't be published to Stream", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
