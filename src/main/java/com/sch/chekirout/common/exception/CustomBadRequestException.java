package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomBadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomBadRequestException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomBadRequestException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }


}
