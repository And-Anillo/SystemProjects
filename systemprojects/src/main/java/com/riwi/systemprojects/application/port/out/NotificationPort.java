package com.riwi.systemprojects.application.port.out;

/**
 * Output port for notifications.
 * This interface defines the contract for notification operations.
 */
public interface NotificationPort {
    void notify(String message);
}
