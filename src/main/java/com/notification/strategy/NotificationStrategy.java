package com.notification.strategy;

import com.notification.model.event.SubscriptionStatusUpdated;

public interface NotificationStrategy {
    boolean canHandle(SubscriptionStatusUpdated event);
    void handle(SubscriptionStatusUpdated event, String email, String name);
} 