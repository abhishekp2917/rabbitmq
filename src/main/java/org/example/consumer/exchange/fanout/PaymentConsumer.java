package org.example.consumer.exchange.fanout;

import org.example.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.order.payment", ackMode = "NONE", concurrency = "3-5")
    public void listenOrderPayment(Order order) {
        System.out.println(String.format("Queue : q.order.payment, Order : %s", order));
    }

    public Order consumeOrderPayment() {
        try {
            Order order = (Order)rabbitTemplate.receiveAndConvert("q.order.payment");
            return order;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
