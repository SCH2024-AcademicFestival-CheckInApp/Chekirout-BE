package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomUnauthorizedException extends RuntimeException {

    private final Errorcode errorcode;

    public CustomUnauthorizedException(String message, Errorcode errorcode) {
        super(message);
        this.errorcode = errorcode;
    }

    public CustomUnauthorizedException(String message, Throwable cause, Errorcode errorcode) {
        super(message, cause);
        this.errorcode = errorcode;
    }
}
