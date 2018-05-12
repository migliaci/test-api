package com.migliaci.myretail.exception;

/**
 * Exception that will be thrown when read/write operation fails.
 * Underlying cause may be IOException, etc.
 *
 * @Author migliaci
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
