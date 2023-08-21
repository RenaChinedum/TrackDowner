package com.ntechinedum.TrackDowner.exception;

public class CustomUserException extends Exception{
    public CustomUserException() {
    }

    public CustomUserException(String message) {
        super(message);
    }

    public CustomUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomUserException(Throwable cause) {
        super(cause);
    }

    public CustomUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
