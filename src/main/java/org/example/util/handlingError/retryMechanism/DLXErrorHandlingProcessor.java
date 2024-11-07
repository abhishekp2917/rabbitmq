package org.example.util.handlingError.retryMechanism;

import com.rabbitmq.client.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.Message;
import org.springframework.lang.NonNull;
import java.io.IOException;

/**
 * Processor for handling error messages in RabbitMQ using a Dead Letter Exchange (DLX) strategy.
 * This class handles the logic for routing messages to a dead-letter exchange after a defined number of retry attempts.
 */
@Setter
@Getter
@Builder
public class DLXErrorHandlingProcessor {

    // The name of the dead-letter exchange where failed messages are routed after exceeding max retries.
    @NonNull
    private String deadExchangeName;

    // Maximum number of retry attempts before a message is sent to the dead-letter exchange.
    private int maxRetryCount = 3;

    /**
     * Constructor that initializes the DLXErrorHandlingProcessor with the specified dead exchange name.
     *
     * @param deadExchangeName The name of the dead-letter exchange.
     * @throws IllegalArgumentException if the exchange name is null or empty.
     */
    public DLXErrorHandlingProcessor(String deadExchangeName) throws IllegalArgumentException {
        super();
        if (deadExchangeName == null || deadExchangeName.length() == 0) {
            throw new IllegalArgumentException("Must define dead exchange name");
        }
        this.deadExchangeName = deadExchangeName;
    }

    /**
     * Constructor that initializes the DLXErrorHandlingProcessor with a specified dead exchange name and max retry count.
     *
     * @param deadExchangeName The name of the dead-letter exchange.
     * @param maxRetryCount The maximum retry count allowed before moving the message to the dead-letter exchange.
     */
    public DLXErrorHandlingProcessor(String deadExchangeName, int maxRetryCount) {
        this(deadExchangeName);
        setMaxRetryCount(maxRetryCount);
    }

    /**
     * Handles an error message by checking its retry count and deciding whether to route it to the dead-letter exchange
     * or reject it for another retry.
     *
     * @param message The RabbitMQ message to handle.
     * @param channel The RabbitMQ channel used for acknowledging or rejecting messages.
     * @param deliveryTag The delivery tag of the message for acknowledgment.
     * @return true if the message was successfully processed, false if an error occurred.
     */
    public boolean handleErrorMessage(Message message, Channel channel, long deliveryTag) {
        var rabbitMqHeader = new RabbitMQHeader(message.getMessageProperties().getHeaders());
        try {
            // Check if the message's retry count has reached or exceeded the maximum allowed.
            if (rabbitMqHeader.getFailedRetryCount() >= maxRetryCount) {
                // Publish the message to the dead-letter exchange and acknowledge its receipt.
                channel.basicPublish(this.getDeadExchangeName(), message.getMessageProperties().getReceivedRoutingKey(), null, message.getBody());
                channel.basicAck(deliveryTag, false);
            } else {
                // Reject the message without requeueing for further retry.
                channel.basicReject(deliveryTag, false);
            }
            return true;
        } catch (IOException e) {
            // Return false to indicate that processing the message failed due to an I/O error.
            return false;
        }
    }

    /**
     * Sets the maximum retry count for the processor.
     *
     * @param maxRetryCount The maximum retry count.
     * @throws IllegalArgumentException if the retry count is greater than 1000 or less than 0.
     */
    public void setMaxRetryCount(int maxRetryCount) throws IllegalArgumentException {
        if (maxRetryCount > 1000 || maxRetryCount < 0) {
            throw new IllegalArgumentException("Max retry count must be between 0-1000");
        }
        this.maxRetryCount = maxRetryCount;
    }
}
