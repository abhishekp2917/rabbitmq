package org.example.service.consumer.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for consuming messages from the text message queue,
 * specifically messages of type String.
 */
@Service
public class TextMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate used for receiving messages from RabbitMQ

    /**
     * This method listens for incoming messages from the text message queue (Q_MESSAGE_TEXT).
     * The message is expected to be of type String.
     *
     * @param message The received message of type String.
     */
    @RabbitListener(
            id = Queue.Q_MESSAGE_TEXT, // Listener id to identify this listener
            queues = Queue.Q_MESSAGE_TEXT, // The name of the queue this listener will listen to
            ackMode = ConsumerAcknowledgementMode.NONE // No automatic acknowledgment for the message
    )
    public void listenTextMessage(String message) {
        // Print the queue name and the message to the console
        System.out.println(String.format("Queue : %s, Message : %s", Queue.Q_MESSAGE_TEXT, message));
    }

    /**
     * This method consumes a message from the specified queue and converts it to a String.
     *
     * @param queueName The name of the queue to consume the message from.
     * @return The consumed message as a String, or null if the queue is empty or no message is found.
     */
    public String consumeTextMessage(String queueName) {
        // Receive and convert the message from the specified queue
        Object message = rabbitTemplate.receiveAndConvert(queueName);

        // Check if the message is not null and return it as a String
        if (message != null) {
            return message.toString();
        } else {
            // Return null if no message was found in the queue
            return null;
        }
    }
}
