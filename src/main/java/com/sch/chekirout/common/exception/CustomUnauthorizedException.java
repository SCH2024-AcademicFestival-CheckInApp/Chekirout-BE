package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomUnauthorizedException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomUnauthorizedException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomUnauthorizedException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
