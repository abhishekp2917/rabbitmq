package org.example.consumer.basic;

import org.example.model.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.message.json", ackMode = "NONE", concurrency = "3-5")
    public void listenJsonMessage(Employee employee) {
        System.out.println(String.format("Queue : q.message.json, Employee : %s", employee));
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
