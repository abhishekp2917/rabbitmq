package org.example.service.consumer.stream.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Stream;
import org.example.model.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service listens to a RabbitMQ stream, specifically for JSON messages related to Employee data.
 * It uses the RabbitMQ Stream feature to consume messages from the specified stream.
 */
@Service
public class JsonMessageStreamConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate for interacting with RabbitMQ

    /**
     * This method listens to messages from the RabbitMQ stream for Employee objects.
     * The message is automatically acknowledged after it is consumed.
     * The stream `Stream.S_MESSAGE_JSON` is used to receive the Employee data.
     *
     * @param employee The Employee object consumed from the stream.
     */
    @RabbitListener(
            id = Stream.S_MESSAGE_JSON, // Unique listener id for this consumer
            queues = Stream.S_MESSAGE_JSON, // Queue to listen to in RabbitMQ stream
            ackMode = ConsumerAcknowledgementMode.AUTO // Automatically acknowledge the message after consumption
    )
    public void listenJsonMessageStream(Employee employee) {
        // Log the consumed message to the console for debugging or monitoring purposes
        System.out.println(String.format("Stream : %s, Employee : %s", Stream.S_MESSAGE_JSON, employee));
    }
}
