package org.example.service.consumer.advance.prefetch;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Audio;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AudioCompressionConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_AUDIO_COMPRESSION,
            containerFactory = "getAudioCompressionQueueContainerFactory",
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "2",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_AUDIO_COMPRESSION,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_AUDIO, type = ExchangeType.FANOUT, durable = "true", autoDelete = "false")
            )
    )
    public void listenAudioCompression(Audio audio) throws InterruptedException {
        Thread.sleep(10000);
        System.out.println(String.format("Queue : %s, Audio : %s", org.example.constants.Queue.Q_AUDIO_COMPRESSION, audio));
    }
}
