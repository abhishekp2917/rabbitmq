package org.example.service.consumer.handlingError.deadLetterExchange;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Encoding;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoEncodingConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_ENCODING_VIDEO,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ENCODING_VIDEO,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC),
                                    @Argument(name = "x-dead-letter-exchange", value = org.example.constants.Exchange.X_ENCODING_DLX),
                                    @Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer")
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_ENCODING, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"),
                    key = "video"
            )
    )
    public void listenVideoEncoding(Encoding encoding) {
        if(encoding!=null && !encoding.getMediaType().equals("MP4")) throw new AmqpRejectAndDontRequeueException("Video format not supported");
        System.out.println(String.format("Queue : %s, Encoding : %s", org.example.constants.Queue.Q_ENCODING_VIDEO, encoding));
    }

    public Encoding consumeVideoEncoding() {
        try {
            Encoding encoding = (Encoding)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ENCODING_VIDEO);
            if(encoding!=null && !encoding.getMediaType().equals("MP4")) throw new AmqpRejectAndDontRequeueException("Video format not supported");
            return encoding;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
