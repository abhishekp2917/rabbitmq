package org.example.consumer.exchange.headers;

import org.example.model.FNOLClaim;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalFNOLClaimConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.claim.normal", ackMode = "NONE", concurrency = "3-5")
    public void listenNormalFNOLClaim(FNOLClaim fnolClaim) {
        System.out.println(String.format("Queue : q.claim.normal, Claim : %s", fnolClaim));
    }

    public FNOLClaim consumeNormalFNOLClaim() {
        try {
            FNOLClaim fnolClaim = (FNOLClaim)rabbitTemplate.receiveAndConvert("q.claim.normal");
            return fnolClaim;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
