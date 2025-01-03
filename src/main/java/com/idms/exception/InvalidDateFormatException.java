package com.idms.exception;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException(String message, Exception e) {
        super(message);
    }
}
