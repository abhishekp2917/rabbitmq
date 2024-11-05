package org.example.service.consumer.stream.offset;

import org.example.constants.Consumer;
import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Stream;
import org.example.model.Video;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.MessageHandler.Context;

@Service
public class VideoEncodingStreamConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_ABSOLUTE_1,
            containerFactory = Consumer.C_STREAM_OFFSET_ABSOLUTE_1,
            queues = Stream.S_VIDEO_ENCODING,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenVideoEncodingAbsoluteOffsetStream(Message message, Context context) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(String.format("Absolute Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_DEFAULT_1,
            queues = Stream.S_VIDEO_ENCODING,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenVideoEncodingDefaultOffsetStream(Video video) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(String.format("Default offset Stream : %s, Video : %s", Stream.S_VIDEO_ENCODING, video));
    }

    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_FIRST_1,
            containerFactory = Consumer.C_STREAM_OFFSET_FIRST_1,
            queues = Stream.S_VIDEO_ENCODING,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenVideoEncodingFirstOffsetStream(Message message, Context context) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(String.format("First Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_LAST_1,
            containerFactory = Consumer.C_STREAM_OFFSET_LAST_1,
            queues = Stream.S_VIDEO_ENCODING,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenVideoEncodingLastOffsetStream(Message message, Context context) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(String.format("Last Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_NEXT_1,
            containerFactory = Consumer.C_STREAM_OFFSET_NEXT_1,
            queues = Stream.S_VIDEO_ENCODING,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenVideoEncodingNextOffsetStream(Message message, Context context) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(String.format("Next offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_TIMESTAMP_1,
            containerFactory = Consumer.C_STREAM_OFFSET_TIMESTAMP_1,
            queues = Stream.S_VIDEO_ENCODING,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenVideoEncodingTimestampOffsetStream(Message message, Context context) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(String.format("Timestamp Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }
}
