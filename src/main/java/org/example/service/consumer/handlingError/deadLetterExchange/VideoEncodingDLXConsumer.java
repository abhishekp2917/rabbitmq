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
    private RabbitTemplate rabbitTemplate; // Used to interact with RabbitMQ.

    /**
     * Listens to the Dead Letter Queue (DLX) for failed video encoding messages.
     * It consumes and processes the messages that could not be processed by the main queue.
     *
     * @param encoding The encoding object that failed in the original processing.
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_ENCODING_VIDEO_DLX, // Unique listener ID for this consumer
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatically acknowledges the message after it's consumed
            concurrency = "3-5", // Specifies concurrency level (3 to 5 consumers for this listener)
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_ENCODING_VIDEO_DLX, // DLX queue name
                            durable = "true", // Ensures the queue survives RabbitMQ restarts
                            exclusive = "false", // The queue is shared among consumers
                            autoDelete = "false", // The queue won't delete itself when no consumers are attached
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Defines the queue type as classic
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_ENCODING_DLX, type = ExchangeType.DIRECT, durable = "true", autoDelete = "false"), // Binding to the DLX exchange
                    key = "video" // Routing key used to direct messages to the DLX queue
            )
    )
    public void listenVideoEncodingDLX(Encoding encoding) {
        // Logs the failed encoding message from the DLX queue for further processing or debugging
        System.out.println(String.format("Queue : %s, Encoding : %s", org.example.constants.Queue.Q_ENCODING_VIDEO_DLX, encoding));
    }

    /**
     * Consumes messages from the Dead Letter Queue (DLX) manually.
     * This method is used for consuming failed messages from the queue.
     *
     * @return Encoding The failed encoding message or null if no message is available.
     */
    public Encoding consumeVideoEncodingDLX() {
        try {
            // Receive and convert the message from the DLX queue into the Encoding object
            Encoding encoding = (Encoding) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_ENCODING_VIDEO_DLX);
            return encoding;
        } catch (Exception ex) {
            // Return null in case of an error or exception
            return null;
        }
    }
}
