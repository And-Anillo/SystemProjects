package com.riwi.systemprojects.infrastructure.adapter.out.notification;

import com.riwi.systemprojects.application.port.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Simple notification adapter that logs to console/file.
 * In a real application, this could send emails, SMS, or push notifications.
 */
@Component
public class NotificationAdapter implements NotificationPort {

    private static final Logger logger = LoggerFactory.getLogger(NotificationAdapter.class);

    @Override
    public void notify(String message) {
        String notification = String.format("[NOTIFICATION] %s - %s", LocalDateTime.now(), message);
        logger.info(notification);
    }
}
