package com.notification.strategy;

import com.notification.model.enums.NotificationReason;
import com.notification.model.enums.SubscriptionEventType;
import com.notification.model.event.SubscriptionStatusUpdated;
import com.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpirationNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public boolean canHandle(SubscriptionStatusUpdated event) {
        boolean canHandle = SubscriptionEventType.EXPIRED.equals(event.getEventType()) &&
                NotificationReason.SUBSCRIPTION_EXPIRED.equals(NotificationReason.valueOf(event.getMetadata().get("reason").toString()));
        log.info("ExpirationNotificationStrategy: canHandle: {} for event: {}", canHandle, event);
        return canHandle;
    }

    @Override
    public void handle(SubscriptionStatusUpdated event, String email, String name) {
        log.info("ExpirationNotificationStrategy: Handling event: {} for email: {} and name: {}", event, email, name);
        emailService.sendExpirationEmail(email, name, event.getSubscriptionId());
        log.info("ExpirationNotificationStrategy: Completed handling event: {}", event);
    }
} 