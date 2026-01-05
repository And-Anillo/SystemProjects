package com.riwi.systemprojects.infrastructure.adapters.output.security;

import com.riwi.systemprojects.domain.ports.out.CurrentUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter that retrieves the current authenticated user from Spring Security
 * context.
 */
@Component
public class CurrentUserAdapter implements CurrentUserPort {

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        // The principal will be the user ID (UUID) set during JWT authentication
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return UUID.fromString((String) principal);
        }

        throw new IllegalStateException("Invalid principal type");
    }
}
