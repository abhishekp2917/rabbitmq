package org.example.util.handlingError.retryMechanism;

import com.rabbitmq.client.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.Message;
import org.springframework.lang.NonNull;
import java.io.IOException;

@Setter
@Getter
@Builder
public class DLXErrorHandlingProcessor {

    @NonNull
    private String deadExchangeName;

    private int maxRetryCount = 3;

    public DLXErrorHandlingProcessor(String deadExchangeName) throws IllegalArgumentException {
        super();
        if (deadExchangeName == null || deadExchangeName.length() == 0) {
            throw new IllegalArgumentException("Must define dead exchange name");
        }
        this.deadExchangeName = deadExchangeName;
    }

    public DLXErrorHandlingProcessor(String deadExchangeName, int maxRetryCount) {
        this(deadExchangeName);
        setMaxRetryCount(maxRetryCount);
    }

    public boolean handleErrorMessage(Message message, Channel channel, long deliveryTag) {
        var rabbitMqHeader = new RabbitMQHeader(message.getMessageProperties().getHeaders());
        try {
            if (rabbitMqHeader.getFailedRetryCount() >= maxRetryCount) {
                channel.basicPublish(this.getDeadExchangeName(), message.getMessageProperties().getReceivedRoutingKey(), null, message.getBody());
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, false);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void setMaxRetryCount(int maxRetryCount) throws IllegalArgumentException {
        if (maxRetryCount > 1000) {
            throw new IllegalArgumentException("Max retry count must be between 0-1000");
        }
        this.maxRetryCount = maxRetryCount;
    }
}
