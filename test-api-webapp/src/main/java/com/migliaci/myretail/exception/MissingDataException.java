package com.migliaci.myretail.exception;

/**
 * Exception that will be thrown when expected data cannot be found
 *
 * @Author migliaci
 */
public class MissingDataException extends RuntimeException {

    public MissingDataException(String message) {
        super(message);
    }
}
