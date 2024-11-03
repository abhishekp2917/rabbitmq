package org.example.service.consumer.basic;

import org.example.constants.Queue;
import org.example.model.Video;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriorityQueueMessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Video consumePriorityQueueMessage() {
        Object message = rabbitTemplate.receiveAndConvert(Queue.Q_PRIORITY_MESSAGE);
        if(message!=null && message instanceof Video) return (Video)message;
        else return null;
    }
}
