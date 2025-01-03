package com.idms.exception;

public class SalesLocationNotFoundException extends RuntimeException {

    public SalesLocationNotFoundException(String message) {
        super(message);
    }

    public SalesLocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
