package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;

public class TokenExpiredException extends CustomBadRequestException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED.getMessage(), ErrorCode.TOKEN_EXPIRED);
    }
}