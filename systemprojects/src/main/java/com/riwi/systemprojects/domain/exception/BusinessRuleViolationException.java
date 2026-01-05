package com.riwi.systemprojects.domain.exception;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessRuleViolationException extends DomainException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
