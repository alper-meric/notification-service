package com.notification.service;

import com.notification.model.event.SubscriptionStatusUpdated;

public interface NotificationService {
    void Notify(SubscriptionStatusUpdated event);
} 