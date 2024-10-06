package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomDuplicatedException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomDuplicatedException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomDuplicatedException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
