package dev.ed.transaction_service.exception;

public class MaxRetriesException extends RuntimeException {
    public MaxRetriesException(String message) {
        super(message);
    }
}
