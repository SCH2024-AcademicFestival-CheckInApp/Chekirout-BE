package com.sch.chekirout.global.presentation;

import com.sch.chekirout.common.exception.ErrorCode;

public record ErrorResponse(String error, ErrorCode errorCode, int code) {

    public ErrorResponse(String error) {
        this(error, null, 0);
    }

    public ErrorResponse(String error, ErrorCode errorCode, int code) {
        this.error = error;
        this.errorCode = errorCode;
        this.code = code;
    }
}
