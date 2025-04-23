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
public class ActivationNotificationStrategy implements NotificationStrategy {
    
    private final EmailService emailService;

    @Override
    public boolean canHandle(SubscriptionStatusUpdated event) {
        boolean canHandle = SubscriptionEventType.ACTIVATED.equals(event.getEventType()) &&
               NotificationReason.PAYMENT_SUCCEEDED.equals(NotificationReason.valueOf(event.getMetadata().get("reason").toString()));
        log.info("ActivationNotificationStrategy: canHandle: {} for event: {}", canHandle, event);
        return canHandle;
    }

    @Override
    public void handle(SubscriptionStatusUpdated event, String email, String name) {
        log.info("ActivationNotificationStrategy: Handling event: {} for email: {} and name: {}", event, email, name);
        emailService.sendActivationEmail(email, name, event.getSubscriptionId());
        log.info("ActivationNotificationStrategy: Completed handling event: {}", event);
    }
} 