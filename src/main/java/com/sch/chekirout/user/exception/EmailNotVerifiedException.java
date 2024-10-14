package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;

public class EmailNotVerifiedException extends CustomBadRequestException {

    public EmailNotVerifiedException() {
        super(ErrorCode.EMAIL_NOT_VERIFIED.getMessage(), ErrorCode.EMAIL_NOT_VERIFIED);
    }
}