package com.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.model.NotificationLog;
import com.notification.model.enums.NotificationReason;
import com.notification.model.enums.Status;
import com.notification.model.event.SubscriptionStatusUpdated;
import com.notification.repository.NotificationRepository;
import com.notification.service.CustomerService;
import com.notification.service.NotificationService;
import com.notification.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerService customerService;
    private final List<NotificationStrategy> notificationStrategies;
    private final ObjectMapper objectMapper;
    private static final int MAX_RETRY_COUNT = 5;

    @Override
    @Transactional
    public void Notify(SubscriptionStatusUpdated event) {
        log.info("Processing notification for subscription event: {}", event);

        try {
            Map<String, String> customerInfo = customerService.getCustomerInfo(event.getUserId());
            String email = customerInfo.get("email");
            String name = customerInfo.get("name");

            NotificationLog notificationLog = NotificationLog.builder()
                    .userId(event.getUserId())
                    .email(email)
                    .name(name)
                    .phoneNumber(customerInfo.get("phoneNumber"))
                    .notificationReason(NotificationReason.valueOf(event.getMetadata().get("reason").toString()))
                    .status(Status.PENDING)
                    .retryCount(0)
                    .build();

            notificationRepository.save(notificationLog);

            try {
                Optional<NotificationStrategy> strategy = notificationStrategies.stream()
                        .filter(s -> s.canHandle(event))
                        .findFirst();

                if (strategy.isPresent()) {
                    strategy.get().handle(event, email, name);
                    notificationLog.setStatus(Status.SENT);
                    notificationLog.setSentAt(LocalDateTime.now());
                } else {
                    log.warn("No suitable notification strategy found for event: {}", event);
                    notificationLog.setStatus(Status.NO_STRATEGY_FOUND);
                    notificationLog.setSubscriptionEvent(event.toString());
                    notificationLog.setRetryCount(MAX_RETRY_COUNT);
                }
            } catch (Exception e) {
                log.error("Error processing notification for subscription: {}", event.getSubscriptionId(), e);
                notificationLog.setStatus(Status.FAILED);
                notificationLog.setSubscriptionEvent(event.toString());
                notificationLog.setRetryCount(notificationLog.getRetryCount() + 1);
            }

            notificationRepository.save(notificationLog);
            log.info("Notification processing completed for subscription: {}", event.getSubscriptionId());

        } catch (Exception e) {
            log.error("Error processing notification for subscription: {}", event.getSubscriptionId(), e);
            throw new RuntimeException("Notification processing failed", e);
        }
    }

    @Scheduled(fixedDelay = 300000) // 5 dakikada bir çalışır
    @Transactional
    public void retryFailedNotifications() {
        log.info("Starting retry process for failed notifications");

        List<NotificationLog> failedNotifications = notificationRepository.findByStatusAndRetryCountLessThan(Status.FAILED, MAX_RETRY_COUNT);

        for (NotificationLog notification : failedNotifications) {
            try {
                SubscriptionStatusUpdated subscriptionStatusUpdated = parseSubscriptionEvent(notification.getSubscriptionEvent());
                Optional<NotificationStrategy> strategy = notificationStrategies.stream()
                        .filter(s -> s.canHandle(subscriptionStatusUpdated))
                        .findFirst();

                if (strategy.isPresent()) {
                    strategy.get().handle(subscriptionStatusUpdated, notification.getEmail(), notification.getName());
                    notification.setStatus(Status.SENT);
                    notification.setSentAt(LocalDateTime.now());
                } else {
                    log.warn("No suitable notification strategy found for retry event: {}", subscriptionStatusUpdated);
                    notification.setStatus(Status.NO_STRATEGY_FOUND);
                    notification.setRetryCount(MAX_RETRY_COUNT);
                }
            } catch (Exception e) {
                log.error("Error retrying notification: {}", notification.getId(), e);
                notification.setRetryCount(notification.getRetryCount() + 1);
            }

            notificationRepository.save(notification);
            log.info("Retry attempt {} for notification: {}", notification.getRetryCount(), notification.getId());
        }

        log.info("Retry process completed. Processed {} notifications", failedNotifications.size());
    }

    public SubscriptionStatusUpdated parseSubscriptionEvent(String json) {
        try {
            return objectMapper.readValue(json, SubscriptionStatusUpdated.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("SubscriptionEvent could not deserialize", e);
        }
    }
} 