package org.example.service.consumer.handlingError.deadLetterExchange;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Encoding;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoEncodingDLXConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_ENCODING_VIDEO_DLX,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ENCODING_VIDEO_DLX,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_ENCODING_DLX, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"),
                    key = "video"
            )
    )
    public void listenVideoEncodingDLX(Encoding encoding) {
        System.out.println(String.format("Queue : %s, Encoding : %s", org.example.constants.Queue.Q_ENCODING_VIDEO_DLX, encoding));
    }

    public Encoding consumeVideoEncodingDLX() {
        try {
            Encoding encoding = (Encoding)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ENCODING_VIDEO_DLX);
            return encoding;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
