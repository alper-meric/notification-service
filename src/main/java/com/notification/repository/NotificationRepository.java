package com.notification.repository;

import com.notification.model.NotificationLog;
import com.notification.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationLog, UUID> {
    List<NotificationLog> findByStatusAndRetryCountLessThan(Status status, Integer retryCount);
} 