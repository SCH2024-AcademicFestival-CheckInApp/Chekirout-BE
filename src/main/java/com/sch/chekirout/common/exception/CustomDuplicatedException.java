package com.sch.chekirout.common.exception;

public abstract class CustomDuplicatedException extends RuntimeException {

    public CustomDuplicatedException(String message) {
        super(message);
    }

    public CustomDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
