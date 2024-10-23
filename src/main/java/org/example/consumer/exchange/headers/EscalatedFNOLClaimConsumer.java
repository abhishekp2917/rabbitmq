package org.example.consumer.exchange.headers;

import org.example.model.FNOLClaim;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EscalatedFNOLClaimConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.claim.escalated", ackMode = "NONE", concurrency = "3-5")
    public void listenEscalatedFNOLClaim(FNOLClaim fnolClaim) {
        System.out.println(String.format("Queue : q.claim.escalated, Claim : %s", fnolClaim));
    }

    public FNOLClaim consumeEscalatedFNOLClaim() {
        try {
            FNOLClaim fnolClaim = (FNOLClaim)rabbitTemplate.receiveAndConvert("q.claim.escalated");
            return fnolClaim;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
