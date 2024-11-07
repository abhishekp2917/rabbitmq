package org.example.config.advance.scheduling;

import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up queues, exchange, and bindings for media uploading tasks.
 *
 * This configuration manages:
 * - Separate queues for media uploads to AWS, GCP, and Azure.
 * - A topic exchange for flexible routing of messages based on cloud provider.
 * - Specific bindings that use routing key patterns to direct messages to the appropriate queue.
 */
@Configuration
public class ScheduleConsumerBeanConfig {

    /**
     * Defines a queue for media uploads to AWS.
     *
     * - The queue is durable, meaning it will persist even if the broker restarts.
     * - Classic queue type is specified for compatibility with standard RabbitMQ queues.
     * - This queue is designated to handle media files specifically being uploaded to AWS.
     *
     * @return Queue object for AWS media uploads.
     */
    @Bean
    public Queue mediaUploadingToAWSQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MEDIA_UPLOAD_AWS, true, false, false, args);
    }

    /**
     * Defines a queue for media uploads to GCP (Google Cloud Platform).
     *
     * - Durable and classic-type configuration like the AWS queue.
     * - This queue is dedicated to handling media files that need to be uploaded to GCP.
     *
     * @return Queue object for GCP media uploads.
     */
    @Bean
    public Queue mediaUploadingToGCPQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MEDIA_UPLOAD_GCP, true, false, false, args);
    }

    /**
     * Defines a queue for media uploads to Azure.
     *
     * - Set up as durable and classic-type queue to ensure consistency across cloud providers.
     * - This queue is responsible for handling media upload messages specifically for Azure.
     *
     * @return Queue object for Azure media uploads.
     */
    @Bean
    public Queue mediaUploadingToAzureQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE, true, false, false, args);
    }

    /**
     * Defines a topic exchange for routing media upload messages.
     *
     * - A topic exchange is ideal for message routing based on a routing key pattern.
     * - This exchange will route messages to different queues based on cloud provider (AWS, GCP, Azure).
     *
     * @return TopicExchange for media uploads.
     */
    @Bean
    public TopicExchange uploadExchange() {
        return new TopicExchange(org.example.constants.Exchange.X_MEDIA, true, false);
    }

    /**
     * Binds the AWS upload queue to the topic exchange with a specific routing key pattern.
     *
     * - The routing key pattern `#.aws.#` directs any message with "aws" in the key to the AWS queue.
     * - `#` is a wildcard that can match zero or more words, allowing flexibility in message routing.
     *
     * @return Binding for AWS media uploads.
     */
    @Bean
    public Binding mediaUploadAWSBinding() {
        return BindingBuilder.bind(mediaUploadingToAWSQueue()).to(uploadExchange()).with("#.aws.#");
    }

    /**
     * Binds the GCP upload queue to the topic exchange with a specific routing key pattern.
     *
     * - The pattern `#.gcp.#` ensures that messages with "gcp" in the routing key are directed to the GCP queue.
     * - This allows GCP-specific messages to be routed to the correct queue automatically.
     *
     * @return Binding for GCP media uploads.
     */
    @Bean
    public Binding mediaUploadGCPBinding() {
        return BindingBuilder.bind(mediaUploadingToGCPQueue()).to(uploadExchange()).with("#.gcp.#");
    }

    /**
     * Binds the Azure upload queue to the topic exchange with a specific routing key pattern.
     *
     * - The pattern `#.azure.#` routes messages with "azure" in the routing key to the Azure queue.
     * - Enables Azure-specific messages to be routed correctly for processing.
     *
     * @return Binding for Azure media uploads.
     */
    @Bean
    public Binding mediaUploadAzureBinding() {
        return BindingBuilder.bind(mediaUploadingToAzureQueue()).to(uploadExchange()).with("#.azure.#");
    }

}
