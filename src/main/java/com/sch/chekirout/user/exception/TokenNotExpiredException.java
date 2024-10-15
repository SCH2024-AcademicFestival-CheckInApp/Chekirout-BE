package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;

public class TokenNotExpiredException extends CustomBadRequestException {
    public TokenNotExpiredException() {
        super(ErrorCode.TOKEN_NOT_EXPIRED.getMessage(), ErrorCode.TOKEN_NOT_EXPIRED);
    }
}