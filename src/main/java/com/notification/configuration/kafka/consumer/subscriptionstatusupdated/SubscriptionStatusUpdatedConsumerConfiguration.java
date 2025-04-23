package com.notification.configuration.kafka.consumer.subscriptionstatusupdated;

import com.notification.configuration.kafka.consumer.BaseConsumerConfiguration;
import com.notification.model.event.SubscriptionStatusUpdated;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SubscriptionStatusUpdatedConsumerConfiguration extends BaseConsumerConfiguration {

    private final SubscriptionStatusUpdatedConsumerProperties properties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String host;

    public SubscriptionStatusUpdatedConsumerConfiguration(SubscriptionStatusUpdatedConsumerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ProducerFactory<String, SubscriptionStatusUpdated> subscriptionStatusUpdatedProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SubscriptionStatusUpdated> subscriptionStatusUpdatedKafkaTemplate() {
        return new KafkaTemplate<>(subscriptionStatusUpdatedProducerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubscriptionStatusUpdated> subscriptionStatusUpdatedKafkaListenerContainerFactory(
            final KafkaTemplate<String, SubscriptionStatusUpdated> kafkaTemplate) {
        return baseKafkaListenerContainerFactory(
                kafkaTemplate,
                host,
                properties.getAutoOffsetReset(),
                properties.getRetryTopic(),
                properties.getConcurrencyLevel(),
                properties.getConsumerGroup(),
                SubscriptionStatusUpdated.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubscriptionStatusUpdated> subscriptionStatusUpdatedKafkaRetryListenerContainerFactory(
            final KafkaTemplate<String, SubscriptionStatusUpdated> kafkaTemplate) {
        return baseKafkaRetryListenerContainerFactory(
                kafkaTemplate,
                host,
                properties.getAutoOffsetReset(),
                properties.getErrorTopic(),
                properties.getConcurrencyLevel(),
                properties.getConsumerGroupRetry(),
                SubscriptionStatusUpdated.class);
    }
}
