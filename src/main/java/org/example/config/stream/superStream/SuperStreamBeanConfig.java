package org.example.config.stream.superStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import org.example.constants.Consumer;
import org.example.constants.Stream;
import org.example.model.Payment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.SuperStream;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

/**
 * Configuration class for setting up SuperStream-related beans for RabbitMQ Stream consumers.
 *
 * In this configuration, we define and set up:
 * - SuperStream configuration for payment notifications.
 * - A RabbitStreamTemplate for producing messages to the "payment" stream.
 * - A StreamListenerContainer for consuming messages from the "payment" stream with a custom offset strategy and message listener.
 */
@Configuration
public class SuperStreamBeanConfig {

    @Autowired
    private ObjectMapper objectMapper; // Used to convert messages to Payment objects

    /**
     * Bean for configuring SuperStream for the 'S_PAYMENT' stream.
     * This SuperStream is a customized stream that enables efficient routing of messages
     * and ensures that messages can be handled and processed efficiently.
     *
     * The SuperStream is initialized with a stream name and a size for message batching (3).
     *
     * @return SuperStream configured for the 'S_PAYMENT' stream
     */
    @Bean
    public SuperStream paymentNotificationSuperStream() {
        // Create a SuperStream for the 'S_PAYMENT' stream, with a batch size of 3
        return new SuperStream(Stream.S_PAYMENT, 3);
    }

    /**
     * Bean for creating a RabbitStreamTemplate for producing messages to the 'S_PAYMENT' stream.
     * This template is responsible for sending messages into RabbitMQ Streams.
     * The messages will be serialized into JSON using the Jackson2JsonMessageConverter.
     *
     * Additionally, a custom routing logic is defined where the routing key is derived
     * from the `Payment` object. The payment's payment method is used to determine the routing.
     * In case of an error, it falls back to routing under the "Other" category.
     *
     * @param environment RabbitMQ stream environment, required for creating the RabbitStreamTemplate
     * @param jackson2JsonMessageConverter JSON message converter used to serialize message bodies
     * @return Configured RabbitStreamTemplate for 'S_PAYMENT' stream
     */
    @Bean
    @Qualifier(Stream.S_PAYMENT)
    public RabbitStreamTemplate getpaymentNotificationRabbitStreamTemplate(Environment environment, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        // Create the RabbitStreamTemplate for sending messages to the 'S_PAYMENT' stream
        var rabbitStreamTemplate = new RabbitStreamTemplate(environment, Stream.S_PAYMENT);
        rabbitStreamTemplate.setMessageConverter(jackson2JsonMessageConverter); // Set the JSON message converter for serialization

        // Set the custom routing logic to route based on the Payment object's payment method
        rabbitStreamTemplate.setSuperStreamRouting(message -> {
            try {
                // Deserialize the message body into a Payment object
                Payment payment = objectMapper.readValue(message.getBodyAsBinary(), Payment.class);
                // Return the payment method for routing
                return payment.getPaymentMethod();
            } catch (Exception ex) {
                // In case of error, route to "Other"
                return "Other";
            }
        });

        // Return the configured RabbitStreamTemplate
        return rabbitStreamTemplate;
    }

    /**
     * Bean for creating a StreamListenerContainer for consuming messages from the 'S_PAYMENT' stream.
     *
     * The listener container listens for messages from the stream and processes them based on the
     * configured consumer name and offset strategy.
     *
     * The container is set to start reading from the next unacknowledged message (OffsetSpecification.next()).
     * This ensures that the consumer picks up where it left off.
     *
     * A custom message listener is set up to process Payment messages, simulating a processing delay
     * by using Thread.sleep(2000) to simulate processing time (e.g., network calls, DB operations).
     *
     * @param environment RabbitMQ stream environment for configuring the listener container
     * @return Configured StreamListenerContainer for consuming from the 'S_PAYMENT' stream
     */
    @Bean
    public StreamListenerContainer paymentSuperStreamListenerContainer(Environment environment) {
        // Create a listener container to consume from the 'S_PAYMENT' stream
        var container = new StreamListenerContainer(environment);

        // Set up the consumer with a custom group and stream
        container.superStream(Stream.S_PAYMENT, Consumer.CG_PAYMENT_SUPER_STREAM_1);

        // Set the consumer's offset strategy to "next", meaning it will process messages after the last acknowledged one
        container.setConsumerCustomizer((id, builder) -> {
            builder.
                    offset(OffsetSpecification.next())  // Use next offset (i.e., after last acknowledged)
                    .autoTrackingStrategy();  // Enable auto tracking to automatically manage offsets
        });

        // Set up a custom message listener for processing payment messages
        container.setupMessageListener(message -> {
            try {
                // Simulate a processing delay
                Thread.sleep(2000); // Simulate delay (e.g., DB or API calls)

                // Deserialize the message body into a Payment object
                Payment payment = objectMapper.readValue(message.getBody(), Payment.class);

                // Log the processed payment (for demonstration purposes)
                System.out.println(String.format("Super Stream : %s, Payment : %s", Stream.S_PAYMENT, payment));
            } catch (Exception ex) {
                // Log any errors that occur during message processing
                System.out.println(String.format("Error occurred : %s", ex.getMessage()));
            }
        });

        // Return the fully configured listener container
        return container;
    }
}
