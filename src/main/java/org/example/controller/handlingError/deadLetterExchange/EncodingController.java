package org.example.controller.handlingError.deadLetterExchange;

import org.example.service.consumer.handlingError.deadLetterExchange.VideoEncodingConsumer;
import org.example.service.consumer.handlingError.deadLetterExchange.VideoEncodingDLXConsumer;
import org.example.model.Encoding;
import org.example.service.producer.handlingError.deadLetterExchange.EncodingProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/encoding")
public class EncodingController {

    @Autowired
    private EncodingProducer encodingProducer;

    @Autowired
    private VideoEncodingConsumer videoEncodingConsumer;

    @Autowired
    private VideoEncodingDLXConsumer videoEncodingDLXConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishEncodingToRMQ(@RequestParam String exchangeName, @RequestParam String routingKey, @RequestBody Encoding encoding) {
        try {
            encodingProducer.publishEncoding(exchangeName, routingKey, encoding);
            return new ResponseEntity<>("Encoding published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Encoding couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/video")
    public ResponseEntity<Encoding> consumeVideoEncodingFromRMQ() {
        try {
            Encoding encoding = videoEncodingConsumer.consumeVideoEncoding();
            if(encoding==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(encoding, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume/video/dlx")
    public ResponseEntity<Encoding> consumeVideoEncodingDLXFromRMQ() {
        try {
            Encoding encoding = videoEncodingDLXConsumer.consumeVideoEncodingDLX();
            if(encoding==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(encoding, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
