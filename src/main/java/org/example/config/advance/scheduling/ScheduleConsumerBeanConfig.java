package org.example.config.advance.scheduling;

import org.example.constants.QueueType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ScheduleConsumerBeanConfig {

    @Bean
    public Queue mediaUploadingToAWSQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MEDIA_UPLOAD_AWS, true, false, false, args);
    }

    @Bean
    public Queue mediaUploadingToGCPQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MEDIA_UPLOAD_GCP, true, false, false, args);
    }

    @Bean
    public Queue mediaUploadingToAzureQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-type", QueueType.CLASSIC);
        return new Queue(org.example.constants.Queue.Q_MEDIA_UPLOAD_AZURE, true, false, false, args);
    }

    @Bean
    public TopicExchange uploadExchange() {
        return new TopicExchange(org.example.constants.Exchange.X_MEDIA, true, false);
    }

    @Bean
    public Binding mediaUploadAWSBinding() {
        return BindingBuilder.bind(mediaUploadingToAWSQueue()).to(uploadExchange()).with("#.aws.#");
    }

    @Bean
    public Binding mediaUploadGCPBinding() {
        return BindingBuilder.bind(mediaUploadingToGCPQueue()).to(uploadExchange()).with("#.gcp.#");
    }

    @Bean
    public Binding mediaUploadAzureBinding() {
        return BindingBuilder.bind(mediaUploadingToAzureQueue()).to(uploadExchange()).with("#.azure.#");
    }

}
