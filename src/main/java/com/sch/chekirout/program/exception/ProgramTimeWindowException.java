package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.PROGRAM_NOT_WITHIN_PARTICIPATION_TIME;

public class ProgramTimeWindowException extends CustomBadRequestException {
    public ProgramTimeWindowException() {
        super(
                PROGRAM_NOT_WITHIN_PARTICIPATION_TIME.getMessage(),
                PROGRAM_NOT_WITHIN_PARTICIPATION_TIME
        );
    }
}
