package org.example.service.consumer.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Queue;
import org.example.model.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for consuming JSON messages from the RabbitMQ queue
 * associated with Employee data.
 */
@Service
public class JsonMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate used for receiving messages from RabbitMQ

    /**
     * This method listens to messages on the `Q_MESSAGE_JSON` queue and deserializes
     * the incoming messages into Employee objects.
     * The `ackMode` is set to AUTO, which means the message is acknowledged automatically
     * when the listener successfully processes it. Concurrency is set to allow 3 to 5
     * consumers to process the messages concurrently.
     *
     * @param employee The Employee object received from the queue
     */
    @RabbitListener(
            id = Queue.Q_MESSAGE_JSON, // Listener ID
            queues = Queue.Q_MESSAGE_JSON, // Queue name to listen to
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatically acknowledge messages
            concurrency = "3-5" // Set concurrency to 3-5 consumers to process messages concurrently
    )
    public void listenJsonMessage(Employee employee) {
        // Log the received message and its corresponding queue name
        System.out.println(String.format("Queue : %s, Employee : %s", Queue.Q_MESSAGE_JSON, employee));
    }

    /**
     * This method consumes a JSON message from a specific queue and returns the deserialized
     * Employee object.
     *
     * @param queueName The name of the queue from which the message will be consumed
     * @return The deserialized Employee object, or null if an error occurs
     */
    public Employee consumeJsonMessage(String queueName) {
        try {
            // Receive the message from the specified queue and convert it to an Employee object
            Employee employee = (Employee) rabbitTemplate.receiveAndConvert(queueName);
            return employee;
        } catch (Exception ex) {
            // Return null in case of any error during message consumption
            return null;
        }
    }
}
