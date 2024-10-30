package org.example.service.consumer.exchange.headers;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.FNOLClaim;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalFNOLClaimConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_CLAIM_NORMAL,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_CLAIM_NORMAL,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_CLAIM, type = ExchangeType.HEADERS, durable = "true", autoDelete = "false"),
                    arguments = {
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "x-match", value = "all"),
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "livable", value = "yes"),
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "fatal", value = "no"),
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "damage", value = "yes")
                    }
            )
    )
    public void listenNormalFNOLClaim(FNOLClaim fnolClaim) {
        System.out.println(String.format("Queue : %s, Claim : %s", org.example.constants.Queue.Q_CLAIM_NORMAL, fnolClaim));
    }

    public FNOLClaim consumeNormalFNOLClaim() {
        try {
            FNOLClaim fnolClaim = (FNOLClaim)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_CLAIM_NORMAL);
            return fnolClaim;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
