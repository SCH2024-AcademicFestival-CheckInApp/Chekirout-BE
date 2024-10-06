package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomForbiddenException extends RuntimeException {

    private final Errorcode errorcode;

    public CustomForbiddenException(String message, Errorcode errorcode) {
        super(message);
        this.errorcode = errorcode;
    }

    public CustomForbiddenException(String message, Throwable cause, Errorcode errorcode) {
        super(message, cause);
        this.errorcode = errorcode;
    }
}
