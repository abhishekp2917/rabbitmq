package org.example.controller.advance.prefetch;

import org.example.model.Audio;
import org.example.service.producer.advance.prefetch.AudioProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/audio")
public class AudioController {

    @Autowired
    private AudioProducer audioProducer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishOrderToRMQ(@RequestBody List<Audio> audios) {
        try {
            audioProducer.publishAudios(audios);
            return new ResponseEntity<>("Audios published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Audios couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
