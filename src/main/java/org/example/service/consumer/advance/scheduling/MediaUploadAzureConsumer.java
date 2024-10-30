package org.example.service.consumer.advance.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Media;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MediaUploadAzureConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Scheduled(cron = "0 5 13 * * *")
    public void startMediaUploadToAzureConsumer() {
        rabbitListenerEndpointRegistry.getListenerContainer(org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE).start();
    }

    @Scheduled(cron = "45 3 13 * * *")
    public void stopMediaUploadToAzureConsumer() {
        rabbitListenerEndpointRegistry.getListenerContainer(org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE).stop();
    }

    @RabbitListener(
            id = org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_MEDIA, type = ExchangeType.TOPIC, durable = "true", autoDelete = "false"),
                    key = "#.azure.#"
            )
    )
    public void listenMediaUploadToAzure(Media media) {
        System.out.println(String.format("Queue : %s, Media : %s", org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE, media));
    }
}
