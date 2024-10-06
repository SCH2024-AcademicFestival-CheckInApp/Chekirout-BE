package com.sch.chekirout.global.presentation;

import com.sch.chekirout.common.exception.Errorcode;

public record ErrorResponse(String error, Errorcode errorcode, int code) {

    public ErrorResponse(String error) {
        this(error, null, 0);
    }

    public ErrorResponse(String error, Errorcode errorcode, int code) {
        this.error = error;
        this.errorcode = errorcode;
        this.code = code;
    }
}
