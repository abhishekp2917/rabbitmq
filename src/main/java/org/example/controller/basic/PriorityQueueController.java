package org.example.controller.basic;

import org.example.model.Video;
import org.example.service.consumer.basic.PriorityQueueMessageConsumer;
import org.example.service.producer.basic.PriorityQueueMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/pq")
public class PriorityQueueController {

    @Autowired
    private PriorityQueueMessageProducer priorityQueueMessageProducer;

    @Autowired
    private PriorityQueueMessageConsumer priorityQueueMessageConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishPriorityMessageToRMQ(@RequestBody List<Video> videos) {
        try {
            priorityQueueMessageProducer.publishPriorityQueueMessages(videos);
            return new ResponseEntity<>("Message published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Message couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume")
    public ResponseEntity<Video> consumePriorityMessageFromRMQ() {
        try {
            Video video = priorityQueueMessageConsumer.consumePriorityQueueMessage();
            if(video==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(video, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
