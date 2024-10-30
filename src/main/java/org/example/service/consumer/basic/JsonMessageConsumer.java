package org.example.service.consumer.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Queue;
import org.example.model.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = Queue.Q_MESSAGE_JSON,
            queues = Queue.Q_MESSAGE_JSON,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5")
    public void listenJsonMessage(Employee employee) {
        System.out.println(String.format("Queue : %s, Employee : %s", Queue.Q_MESSAGE_JSON, employee));
    }

    public Employee consumeJsonMessage(String queueName) {
        try {
            Employee employee = (Employee)rabbitTemplate.receiveAndConvert(queueName);
            return employee;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
