package com.riwi.systemprojects.application.port.out;

import java.util.UUID;

/**
 * Output port for audit logging.
 * This interface defines the contract for audit operations.
 */
public interface AuditLogPort {
    void register(String action, UUID entityId);
}
