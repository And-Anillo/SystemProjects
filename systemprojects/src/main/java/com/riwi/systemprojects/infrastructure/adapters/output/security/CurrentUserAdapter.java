package com.riwi.systemprojects.infrastructure.adapters.output.security;

import com.riwi.systemprojects.domain.exception.UnauthorizedAccessException;
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

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedAccessException("Authentication required to perform this action");
        }

        Object principal = authentication.getPrincipal();
        try {
            if (principal instanceof String) {
                return UUID.fromString((String) principal);
            }
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedAccessException("Invalid user identity in security context");
        }

        throw new UnauthorizedAccessException("No valid authentication found");
    }
}
