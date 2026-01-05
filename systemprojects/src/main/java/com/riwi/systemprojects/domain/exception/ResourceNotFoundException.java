package com.riwi.systemprojects.domain.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
