package org.example.service.consumer.stream.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Stream;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service listens to a RabbitMQ stream for text messages.
 * It consumes the messages from the specified stream and logs them to the console.
 */
@Service
public class TextMessageStreamConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate for interacting with RabbitMQ

    /**
     * This method listens for text messages from the specified RabbitMQ stream.
     * The message is automatically acknowledged after being consumed.
     *
     * @param message The text message consumed from the stream.
     */
    @RabbitListener(
            id = Stream.S_MESSAGE_TEXT, // Unique listener ID for this consumer
            queues = Stream.S_MESSAGE_TEXT, // Queue to listen to in RabbitMQ stream
            ackMode = ConsumerAcknowledgementMode.AUTO // Automatically acknowledge the message after consumption
    )
    public void listenTextMessageStream(String message) {
        // Log the consumed message to the console for debugging or monitoring purposes
        System.out.println(String.format("Stream : %s, Message : %s", Stream.S_MESSAGE_TEXT, message));
    }
}
