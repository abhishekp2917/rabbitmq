package org.example.service.consumer.advance.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constants.Queue;
import org.example.model.Media;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MediaUploadGCPConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void listenMediaUploadToGCP() {
        try {
            Media media = objectMapper.convertValue(rabbitTemplate.receiveAndConvert(Queue.Q_MEDIA_UPLOAD_GCP), Media.class);
            if(media!=null) {
                System.out.println(String.format("Queue : %s, Media : %s", Queue.Q_MEDIA_UPLOAD_GCP, media));
            }
        }
        catch (Exception ex) {
            System.out.println(String.format("Error occurred : %s", ex.getMessage()));
        }
    }
}
