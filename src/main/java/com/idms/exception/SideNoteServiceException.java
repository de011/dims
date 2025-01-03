package com.idms.exception;

public class SideNoteServiceException extends RuntimeException{
    public SideNoteServiceException(String message) {
        super(message);
    }

    public SideNoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
