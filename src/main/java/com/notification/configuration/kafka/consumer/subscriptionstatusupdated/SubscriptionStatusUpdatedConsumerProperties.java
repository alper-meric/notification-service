package com.notification.configuration.kafka.consumer.subscriptionstatusupdated;

import com.notification.configuration.kafka.consumer.BaseConsumerProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.consumer.topics.subscription-status-updated")
@Getter
@Setter
public class SubscriptionStatusUpdatedConsumerProperties extends BaseConsumerProperties {

}
