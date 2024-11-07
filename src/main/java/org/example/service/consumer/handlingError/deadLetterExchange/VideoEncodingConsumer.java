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
    private RabbitTemplate rabbitTemplate; // RabbitTemplate for interacting with RabbitMQ.

    /**
     * Listens to the video encoding queue for messages to process.
     * If the video format is not MP4, the message is rejected and will not be requeued.
     *
     * @param encoding The encoding object containing the video details.
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_ENCODING_VIDEO, // Unique listener ID for this consumer
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatically acknowledges the message after it's consumed
            concurrency = "3-5", // Specifies the concurrency level for this listener (3 to 5 consumers)
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ENCODING_VIDEO, // Queue name for encoding videos
                            durable = "true", // Ensures the queue survives RabbitMQ restarts
                            exclusive = "false", // The queue is shared among consumers
                            autoDelete = "false", // The queue won't delete itself when no consumers are attached
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC), // Defines queue type
                                    @Argument(name = "x-dead-letter-exchange", value = org.example.constants.Exchange.X_ENCODING_DLX), // Defines the DLX (Dead Letter Exchange)
                                    @Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer") // Sets TTL (Time to Live) for messages in the queue (10 seconds)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_ENCODING, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"), // Binding the queue to an exchange
                    key = "video" // Routing key for the binding
            )
    )
    public void listenVideoEncoding(Encoding encoding) {
        // Check if the encoding media type is MP4, if not reject the message and don't requeue it.
        if (encoding != null && !encoding.getMediaType().equals("MP4")) {
            // Reject the message and don't requeue it (it will be moved to the DLX).
            throw new AmqpRejectAndDontRequeueException("Video format not supported");
        }
        // Log the message content for debugging or monitoring purposes
        System.out.println(String.format("Queue : %s, Encoding : %s", org.example.constants.Queue.Q_ENCODING_VIDEO, encoding));
    }

    /**
     * Consumes video encoding messages from the specified queue.
     * If the video format is not MP4, it throws an exception and does not requeue the message.
     *
     * @return Encoding The video encoding object or null if no message is received.
     */
    public Encoding consumeVideoEncoding() {
        try {
            // Receive and convert the message from the queue to the Encoding object.
            Encoding encoding = (Encoding) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ENCODING_VIDEO);
            // If the media type is not MP4, reject the message and don't requeue it.
            if (encoding != null && !encoding.getMediaType().equals("MP4")) {
                throw new AmqpRejectAndDontRequeueException("Video format not supported");
            }
            return encoding;
        } catch (Exception ex) {
            // Return null in case of an error or exception
            return null;
        }
    }
}
