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
public class EscalatedFNOLClaimConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(
            id = org.example.constants.Queue.Q_CLAIM_ESCALATED,
            ackMode = ConsumerAcknowledgementMode.AUTO,
            concurrency = "3-5",
            bindings = @QueueBinding(
                    value = @Queue(
                            value = org.example.constants.Queue.Q_CLAIM_ESCALATED,
                            durable = "true",
                            exclusive = "false",
                            autoDelete = "false",
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC)
                            }),
                    exchange = @Exchange(value = org.example.constants.Exchange.X_CLAIM, type = ExchangeType.HEADERS, durable = "true", autoDelete = "false"),
                    arguments = {
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "x-match", value = "any"),
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "livable", value = "no"),
                            @org.springframework.amqp.rabbit.annotation.Argument(name = "fatal", value = "yes")
                    }
            )
    )
    public void listenEscalatedFNOLClaim(FNOLClaim fnolClaim) {
        System.out.println(String.format("Queue : %s : %s", org.example.constants.Queue.Q_CLAIM_ESCALATED, fnolClaim));
    }

    public FNOLClaim consumeEscalatedFNOLClaim() {
        try {
            FNOLClaim fnolClaim = (FNOLClaim)rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_CLAIM_ESCALATED);
            return fnolClaim;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
