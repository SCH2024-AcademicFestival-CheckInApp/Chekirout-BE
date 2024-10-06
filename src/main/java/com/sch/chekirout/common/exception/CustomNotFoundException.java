package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomNotFoundException extends RuntimeException {

    private final Errorcode errorcode;

    public CustomNotFoundException(String message, Errorcode errorcode) {
        super(message);
        this.errorcode = errorcode;
    }

    public CustomNotFoundException(String message, Throwable cause, Errorcode errorcode) {
        super(message, cause);
        this.errorcode = errorcode;
    }
}
