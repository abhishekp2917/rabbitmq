package org.example.service.consumer.advance.prefetch;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Audio;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service listens for audio compression messages from a fanout exchange.
 * It processes these messages from the queue and ensures the consumer handles them properly
 * by acknowledging the receipt of the messages after processing.
 */
@Service
public class AudioCompressionConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Listens to the queue for audio compression messages.
     * This method processes the received `Audio` messages, simulating a delay (e.g., for compression).
     * It uses a custom container factory for better message handling and allows concurrency for parallel processing.
     * The acknowledgment mode is set to AUTO, meaning messages are acknowledged automatically once processed.
     *
     * @param audio The audio object received from the queue to be processed
     * @throws InterruptedException if the thread is interrupted during the simulated delay
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_AUDIO_COMPRESSION, // Unique ID for this consumer
            containerFactory = "getAudioCompressionQueueContainerFactory", // Custom container factory for configuring the listener
            ackMode = ConsumerAcknowledgementMode.AUTO, // Acknowledgment mode is automatic after processing
            concurrency = "2", // Set the concurrency to 2, meaning 2 threads can consume messages in parallel
            bindings = @QueueBinding(
                    // Queue configuration
                    value = @Queue(
                            value = org.example.constants.Queue.Q_AUDIO_COMPRESSION, // Queue name for audio compression messages
                            durable = "true", // Make the queue durable to survive restarts
                            exclusive = "false", // The queue can be used by other consumers
                            autoDelete = "false", // Queue will not auto-delete when no consumers are connected
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Specify queue type as CLASSIC (standard queue)
                            }),
                    // Bind the queue to an exchange
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_AUDIO, // The name of the exchange
                            type = ExchangeType.FANOUT, // The exchange type is FANOUT, meaning all messages are broadcasted to all queues bound to it
                            durable = "true", // The exchange is durable to survive restarts
                            autoDelete = "false" // The exchange will not be deleted automatically
                    )
            )
    )
    public void listenAudioCompression(Audio audio) throws InterruptedException {
        // Simulate a delay (e.g., audio compression processing) for 10 seconds
        Thread.sleep(10000); // Simulating processing time

        // Log the received audio message for debugging purposes
        System.out.println(String.format("Queue : %s, Audio : %s", org.example.constants.Queue.Q_AUDIO_COMPRESSION, audio));
    }
}
