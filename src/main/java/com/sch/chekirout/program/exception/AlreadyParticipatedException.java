package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.ALREADY_PARTICIPATED;

public class AlreadyParticipatedException extends CustomBadRequestException {
    public AlreadyParticipatedException(final String message) {
        super(
                String.format("이미 참여한 프로그램입니다. 프로그램 이름 : %s", message),
                ALREADY_PARTICIPATED
        );
    }
}
