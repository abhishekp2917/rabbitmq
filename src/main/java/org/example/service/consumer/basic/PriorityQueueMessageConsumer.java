package org.example.service.consumer.basic;

import org.example.constants.Queue;
import org.example.model.Video;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for consuming messages from the priority queue,
 * specifically messages of type Video.
 */
@Service
public class PriorityQueueMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate used for receiving messages from RabbitMQ

    /**
     * This method consumes a message from the priority queue (Q_PRIORITY_MESSAGE) and
     * converts it into a Video object.
     *
     * @return The Video object if the message is successfully received and converted, or null if
     *         the message is either not of type Video or no message is available.
     */
    public Video consumePriorityQueueMessage() {
        // Receive and convert the message from the priority queue (Q_PRIORITY_MESSAGE)
        Object message = rabbitTemplate.receiveAndConvert(Queue.Q_PRIORITY_MESSAGE);

        // Check if the message is not null and is of type Video
        if (message != null && message instanceof Video) {
            // Return the Video object if the message is valid
            return (Video) message;
        } else {
            // Return null if the message is not of type Video or no message was found
            return null;
        }
    }
}
