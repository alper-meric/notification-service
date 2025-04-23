package com.notification.strategy;

import com.notification.model.enums.NotificationReason;
import com.notification.model.enums.SubscriptionEventType;
import com.notification.model.event.SubscriptionStatusUpdated;
import com.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancellationNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;

    @Override
    public boolean canHandle(SubscriptionStatusUpdated event) {
        boolean canHandle = SubscriptionEventType.CANCELLED.equals(event.getEventType());
        log.info("CancellationNotificationStrategy: canHandle: {} for event: {}", canHandle, event);
        return canHandle;
    }

    @Override
    public void handle(SubscriptionStatusUpdated event, String email, String name) {
        log.info("CancellationNotificationStrategy: Handling event: {} for email: {} and name: {}", event, email, name);
        Object reason = event.getMetadata().get("reason");
        NotificationReason notificationReason = NotificationReason.valueOf(reason.toString());
        if (NotificationReason.USER_REQUESTED.equals(notificationReason)) {
            log.info("CancellationNotificationStrategy: Sending user requested cancellation email");
            emailService.sendCancellationByUserEmail(email, name, event.getSubscriptionId());
        } else if (NotificationReason.PAYMENT_FAILED.equals(notificationReason)) {
            log.info("CancellationNotificationStrategy: Sending payment failed email");
            emailService.sendPaymentFailedEmail(email, name, event.getSubscriptionId());
        }
        log.info("CancellationNotificationStrategy: Completed handling event: {}", event);
    }
} 