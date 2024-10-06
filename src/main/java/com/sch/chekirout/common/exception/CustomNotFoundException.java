package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomNotFoundException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
