package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomBadRequestException extends RuntimeException {

    private final Errorcode errorcode;

    public CustomBadRequestException(String message, Errorcode errorcode) {
        super(message);
        this.errorcode = errorcode;
    }

    public CustomBadRequestException(String message, Throwable cause, Errorcode errorcode) {
        super(message, cause);
        this.errorcode = errorcode;
    }
}
