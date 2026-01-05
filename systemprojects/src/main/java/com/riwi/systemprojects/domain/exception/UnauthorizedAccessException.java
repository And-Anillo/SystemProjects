package com.riwi.systemprojects.domain.exception;

/**
 * Exception thrown when a user tries to access or modify a resource they don't
 * own.
 */
public class UnauthorizedAccessException extends DomainException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
