package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;

public class PasswordMismatchException extends CustomBadRequestException {
    public PasswordMismatchException() {
        super(
                ErrorCode.PASSWORD_MISMATCH.getMessage(),
                ErrorCode.PASSWORD_MISMATCH
        );
    }
}
