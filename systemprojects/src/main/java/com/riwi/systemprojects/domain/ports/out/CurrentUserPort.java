package com.riwi.systemprojects.domain.ports.out;

import java.util.UUID;

/**
 * Output port for getting the current authenticated user.
 * This interface defines the contract for security operations.
 */
public interface CurrentUserPort {
    UUID getCurrentUserId();
}
