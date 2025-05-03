package dev.ed.account_service.exception;

public class MaxRetriesException extends RuntimeException {
    public MaxRetriesException(String message) {
        super(message);
    }
}
