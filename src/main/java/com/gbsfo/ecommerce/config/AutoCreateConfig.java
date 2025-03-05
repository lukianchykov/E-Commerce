package com.gbsfo.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateConfig {

    @Value("${spring.kafka.topic.order}")
    private String orderTopic;

    @Value("${spring.kafka.topic.item}")
    private String itemTopic;

    @Value("${spring.kafka.topic.payment}")
    private String paymentTopic;

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
            .name(orderTopic)
            .partitions(3)
            .replicas(3)
            .build();
    }

    @Bean
    public NewTopic itemTopic() {
        return TopicBuilder
            .name(itemTopic)
            .partitions(3)
            .replicas(3)
            .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder
            .name(paymentTopic)
            .partitions(3)
            .replicas(3)
            .build();
    }
}