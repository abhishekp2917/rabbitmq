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

/**
 * This service handles consumption of messages from RabbitMQ Stream, specifically related to video encoding tasks.
 * It demonstrates various offset strategies for consuming messages from the stream (absolute, default, first, last, next, and timestamp).
 */
@Service
public class VideoEncodingStreamConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate for interacting with RabbitMQ

    /**
     * Consumes messages from the video encoding stream with an absolute offset.
     * This listener reads messages based on the exact offset position provided.
     *
     * @param message The RabbitMQ stream message.
     * @param context The message context, which provides metadata such as offset.
     * @throws InterruptedException If the thread is interrupted during processing.
     */
    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_ABSOLUTE_1, // Unique listener id for absolute offset stream
            containerFactory = Consumer.C_STREAM_OFFSET_ABSOLUTE_1, // Container factory for configuring the listener
            queues = Stream.S_VIDEO_ENCODING, // Queue to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO // Acknowledgment mode set to automatic
    )
    public void listenVideoEncodingAbsoluteOffsetStream(Message message, Context context) throws InterruptedException {
        // Simulate processing delay
        Thread.sleep(2000);
        // Log the stream details and message offset
        System.out.println(String.format("Absolute Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    /**
     * Consumes messages from the video encoding stream with the default offset strategy.
     * This listener reads messages from the stream without specifying an offset strategy.
     *
     * @param video The video object received from the stream.
     * @throws InterruptedException If the thread is interrupted during processing.
     */
    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_DEFAULT_1, // Unique listener id for default offset stream
            queues = Stream.S_VIDEO_ENCODING, // Queue to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO // Acknowledgment mode set to automatic
    )
    public void listenVideoEncodingDefaultOffsetStream(Video video) throws InterruptedException {
        // Simulate processing delay
        Thread.sleep(2000);
        // Log the stream details
        System.out.println(String.format("Default offset Stream : %s, Video : %s", Stream.S_VIDEO_ENCODING, video));
    }

    /**
     * Consumes messages from the video encoding stream starting from the first available offset.
     * This listener reads the first message in the stream.
     *
     * @param message The RabbitMQ stream message.
     * @param context The message context, which provides metadata such as offset.
     * @throws InterruptedException If the thread is interrupted during processing.
     */
    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_FIRST_1, // Unique listener id for first offset stream
            containerFactory = Consumer.C_STREAM_OFFSET_FIRST_1, // Container factory for configuring the listener
            queues = Stream.S_VIDEO_ENCODING, // Queue to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO // Acknowledgment mode set to automatic
    )
    public void listenVideoEncodingFirstOffsetStream(Message message, Context context) throws InterruptedException {
        // Simulate processing delay
        Thread.sleep(2000);
        // Log the stream details and message offset
        System.out.println(String.format("First Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    /**
     * Consumes messages from the video encoding stream starting from the last available offset.
     * This listener reads the last message in the stream.
     *
     * @param message The RabbitMQ stream message.
     * @param context The message context, which provides metadata such as offset.
     * @throws InterruptedException If the thread is interrupted during processing.
     */
    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_LAST_1, // Unique listener id for last offset stream
            containerFactory = Consumer.C_STREAM_OFFSET_LAST_1, // Container factory for configuring the listener
            queues = Stream.S_VIDEO_ENCODING, // Queue to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO // Acknowledgment mode set to automatic
    )
    public void listenVideoEncodingLastOffsetStream(Message message, Context context) throws InterruptedException {
        // Simulate processing delay
        Thread.sleep(2000);
        // Log the stream details and message offset
        System.out.println(String.format("Last Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    /**
     * Consumes messages from the video encoding stream starting from the next available offset.
     * This listener reads the message that comes after the last consumed message.
     *
     * @param message The RabbitMQ stream message.
     * @param context The message context, which provides metadata such as offset.
     * @throws InterruptedException If the thread is interrupted during processing.
     */
    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_NEXT_1, // Unique listener id for next offset stream
            containerFactory = Consumer.C_STREAM_OFFSET_NEXT_1, // Container factory for configuring the listener
            queues = Stream.S_VIDEO_ENCODING, // Queue to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO // Acknowledgment mode set to automatic
    )
    public void listenVideoEncodingNextOffsetStream(Message message, Context context) throws InterruptedException {
        // Simulate processing delay
        Thread.sleep(2000);
        // Log the stream details and message offset
        System.out.println(String.format("Next offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }

    /**
     * Consumes messages from the video encoding stream based on a timestamp offset.
     * This listener reads the message based on the provided timestamp offset.
     *
     * @param message The RabbitMQ stream message.
     * @param context The message context, which provides metadata such as offset.
     * @throws InterruptedException If the thread is interrupted during processing.
     */
    @RabbitListener(
            id = Consumer.C_STREAM_OFFSET_TIMESTAMP_1, // Unique listener id for timestamp offset stream
            containerFactory = Consumer.C_STREAM_OFFSET_TIMESTAMP_1, // Container factory for configuring the listener
            queues = Stream.S_VIDEO_ENCODING, // Queue to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO // Acknowledgment mode set to automatic
    )
    public void listenVideoEncodingTimestampOffsetStream(Message message, Context context) throws InterruptedException {
        // Simulate processing delay
        Thread.sleep(2000);
        // Log the stream details and message offset
        System.out.println(String.format("Timestamp Offset Stream : %s, Video : %s, Offset : %s", Stream.S_VIDEO_ENCODING, message.getBody(), context.offset()));
    }
}
