package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;

public class EmailAlreadyExists extends CustomBadRequestException {
    public EmailAlreadyExists() {
        super(
                EMAIL_ALREADY_EXISTS.getMessage(),
                EMAIL_ALREADY_EXISTS
        );
    }
}
