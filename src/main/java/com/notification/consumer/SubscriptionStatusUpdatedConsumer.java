package com.notification.consumer;

import com.notification.model.enums.SubscriptionEventType;
import com.notification.model.event.SubscriptionStatusUpdated;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionStatusUpdatedConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.subscription-status-updated.topic}",
            groupId = "${spring.kafka.consumer.topics.subscription-status-updated.consumerGroup}",
            containerFactory = "subscriptionStatusUpdatedKafkaListenerContainerFactory"
    )
    public void consumeSubscriptionStatusUpdatedEvent(ConsumerRecord<String, SubscriptionStatusUpdated> record) {
        consumeSubscriptionEvent(record, "consumeSubscriptionStatusUpdatedEvent");
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.subscription-status-updated.retryTopic}",
            groupId = "${spring.kafka.consumer.topics.subscription-status-updated.consumerGroupRetry}",
            containerFactory = "subscriptionStatusUpdatedKafkaRetryListenerContainerFactory"
    )
    public void retrySubscriptionStatusUpdatedEvent(ConsumerRecord<String, SubscriptionStatusUpdated> record) {
        consumeSubscriptionEvent(record, "retrySubscriptionStatusUpdatedEvent");
    }

    private void consumeSubscriptionEvent(ConsumerRecord<String, SubscriptionStatusUpdated> record, String methodName) {
        try {
            SubscriptionStatusUpdated event = record.value();
            log.info("Received subscription event: {} in method: {}", event, methodName);

            if (SubscriptionEventType.CREATED.equals(event.getEventType()) ||
                    SubscriptionEventType.RENEWAL.equals(event.getEventType())) {
                log.info("Received subscription event ignored: {} in method: {}", event, methodName);
                return;
            }

            notificationService.Notify(event);


        } catch (Exception e) {
            log.error("Error processing subscription event in topic: {}, partition: {}, offset: {}, method: {}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    methodName,
                    e
            );
            throw e;
        }
    }
}


