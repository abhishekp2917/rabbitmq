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
 * This service listens for audio encoding messages from a fanout exchange.
 * It processes the audio encoding messages, acknowledging them after processing.
 * It uses prefetch settings to manage the concurrency for better performance.
 */
@Service
public class AudioEncodingConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Listens to the queue for audio encoding messages.
     * The method processes the `Audio` message and simulates a short processing time.
     * It uses concurrency to handle multiple messages in parallel.
     * The acknowledgment mode is set to AUTO, meaning messages are acknowledged automatically once processed.
     *
     * @param audio The audio object received from the queue to be processed
     * @throws InterruptedException if the thread is interrupted during the simulated delay
     */
    @RabbitListener(
            id = org.example.constants.Queue.Q_AUDIO_ENCODING, // Unique ID for this listener
            ackMode = ConsumerAcknowledgementMode.AUTO, // Acknowledgment mode is set to AUTO for automatic acknowledgment after processing
            concurrency = "2", // Set the concurrency to 2 to allow 2 threads to process messages in parallel
            bindings = @QueueBinding(
                    // Queue configuration
                    value = @Queue(
                            value = org.example.constants.Queue.Q_AUDIO_ENCODING, // Queue name for audio encoding messages
                            durable = "true", // The queue is durable to survive restarts
                            exclusive = "false", // The queue can be shared by other consumers
                            autoDelete = "false", // The queue will not be automatically deleted
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // The queue type is set to CLASSIC
                            }),
                    // Bind the queue to an exchange
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_AUDIO, // Name of the exchange
                            type = ExchangeType.FANOUT, // The exchange type is FANOUT, meaning all bound queues receive the same message
                            durable = "true", // The exchange is durable to survive restarts
                            autoDelete = "false" // The exchange will not be deleted automatically
                    )
            )
    )
    public void listenAudioEncoding(Audio audio) throws InterruptedException {
        // Simulate a short delay of 1 second (e.g., for encoding or processing the audio)
        Thread.sleep(1000);

        // Log the received audio message for debugging or monitoring purposes
        System.out.println(String.format("Queue : %s, Audio : %s", org.example.constants.Queue.Q_AUDIO_ENCODING, audio));
    }
}
