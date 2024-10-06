package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomDuplicatedException extends RuntimeException {

    private final Errorcode errorcode;

    public CustomDuplicatedException(String message, Errorcode errorcode) {
        super(message);
        this.errorcode = errorcode;
    }

    public CustomDuplicatedException(String message, Throwable cause, Errorcode errorcode) {
        super(message, cause);
        this.errorcode = errorcode;
    }
}
