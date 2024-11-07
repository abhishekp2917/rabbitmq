package org.example.service.consumer.exchange.topic;

import org.example.constants.ConsumerAcknowledgementMode;
import org.example.constants.ExchangeType;
import org.example.constants.QueueType;
import org.example.model.Notification;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpNotificationConsumer {

    // Injecting RabbitTemplate for sending and receiving messages from RabbitMQ.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * This method listens for messages from the Q_NOTIFICATION_OTP queue.
     * The queue is bound to a TOPIC exchange, and the routing key pattern is "#.otp.#".
     * This means that it will listen to all messages whose routing key contains the "otp" word anywhere in the key.
     */
    @RabbitListener(
            // 'id' is a unique identifier for the listener attached to the Q_NOTIFICATION_OTP queue.
            id = org.example.constants.Queue.Q_NOTIFICATION_OTP, // Listener ID for the Q_NOTIFICATION_OTP queue.

            // 'ackMode' specifies how the message acknowledgment should occur.
            // AUTO means RabbitMQ will automatically acknowledge the message once it's consumed.
            ackMode = ConsumerAcknowledgementMode.AUTO, // Automatic acknowledgment after message processing.

            // 'concurrency' defines the number of concurrent consumers to handle the messages.
            // Here, the system will use between 3 to 5 consumers for parallel processing.
            concurrency = "3-5", // 3 to 5 concurrent consumers.

            // 'bindings' define the relationship between the queue and the exchange.
            // This defines how the message will be routed from the exchange to the queue.
            bindings = @QueueBinding(
                    // Defining the queue properties like name, durability, exclusivity, and auto-deletion.
                    value = @Queue(
                            value = org.example.constants.Queue.Q_NOTIFICATION_OTP, // The queue name.
                            durable = "true", // Ensures the queue survives RabbitMQ restarts.
                            exclusive = "false", // The queue is not exclusive to a single consumer.
                            autoDelete = "false", // The queue won't be automatically deleted.
                            arguments = {
                                    @Argument(name = "x-queue-type", value = QueueType.CLASSIC) // Specifies the queue type as CLASSIC.
                            }),

                    // The exchange this queue is bound to.
                    // The exchange type is TOPIC, meaning messages will be routed based on patterns in the routing key.
                    exchange = @Exchange(
                            value = org.example.constants.Exchange.X_NOTIFICATION, // The exchange name.
                            type = ExchangeType.TOPIC, // The exchange type is TOPIC.
                            durable = "true", // Ensures the exchange survives RabbitMQ restarts.
                            autoDelete = "false" // The exchange won't be deleted automatically.
                    ),

                    // The routing key pattern defines which messages are routed to the queue.
                    // The key pattern "#.otp.#" means that any routing key containing the word "otp" will match.
                    key = "#.otp.#" // Routing key pattern for the topic exchange.
            )
    )
    /**
     * This method is triggered when a message arrives in the Q_NOTIFICATION_OTP queue.
     * The message is automatically converted into a 'Notification' object and passed to this method.
     */
    public void listenOtpNotification(Notification notification) {
        // Log the received Notification object. This is useful for tracking and debugging.
        System.out.println(String.format("Queue : %s, Notification : %s", org.example.constants.Queue.Q_NOTIFICATION_OTP, notification));
    }

    /**
     * This method allows manual message consumption.
     * It uses RabbitTemplate to fetch and convert a message from the Q_NOTIFICATION_OTP queue.
     * The method is blocking and returns null if there is an error or no message is available.
     */
    public Notification consumeOtpNotification() {
        try {
            // Attempt to receive and convert a message from the queue to a Notification object.
            Notification notification = (Notification) rabbitTemplate.receiveAndConvert(org.example.constants.Queue.Q_NOTIFICATION_OTP);
            return notification; // Return the converted Notification object.
        } catch (Exception ex) {
            // If an error occurs (e.g., no message or conversion failure), return null.
            return null; // Return null if no message is available or if there's an error.
        }
    }
}
