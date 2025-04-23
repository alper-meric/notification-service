package com.notification.service;

public interface EmailService {
    void sendActivationEmail(String email, String name, String subscriptionId);
    void sendCancellationByUserEmail(String email, String name, String subscriptionId);
    void sendPaymentFailedEmail(String email, String name, String subscriptionId);
    void sendExpirationEmail(String email, String name, String subscriptionId);
} 