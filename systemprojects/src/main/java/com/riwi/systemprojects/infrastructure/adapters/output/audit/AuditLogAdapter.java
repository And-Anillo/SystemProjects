package com.riwi.systemprojects.infrastructure.adapters.output.audit;

import com.riwi.systemprojects.domain.ports.out.AuditLogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Simple audit adapter that logs to console/file.
 * In a real application, this could write to a database or external audit
 * service.
 */
@Component
public class AuditLogAdapter implements AuditLogPort {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogAdapter.class);

    @Override
    public void register(String action, UUID entityId) {
        String auditMessage = String.format("[AUDIT] %s - Action: %s - EntityId: %s",
                LocalDateTime.now(), action, entityId);
        logger.info(auditMessage);
    }
}
