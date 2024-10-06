package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomForbiddenException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomForbiddenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomForbiddenException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
