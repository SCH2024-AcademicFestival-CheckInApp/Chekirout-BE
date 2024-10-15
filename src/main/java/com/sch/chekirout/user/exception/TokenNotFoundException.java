package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;


public class TokenNotFoundException extends CustomBadRequestException {
    public TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND.getMessage(), ErrorCode.TOKEN_NOT_FOUND);
    }
}