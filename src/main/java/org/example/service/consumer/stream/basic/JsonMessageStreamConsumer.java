package org.example.service.consumer.stream.basic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.Stream;
import org.example.model.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonMessageStreamConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = Stream.S_MESSAGE_JSON,
            queues = Stream.S_MESSAGE_JSON,
            ackMode = ConsumerAcknowledgementMode.AUTO)
    public void listenJsonMessageStream(Employee employee) {
        System.out.println(String.format("Stream : %s, Employee : %s", Stream.S_MESSAGE_JSON, employee));
    }
}
