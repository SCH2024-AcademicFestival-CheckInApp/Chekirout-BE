package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.Errorcode.PROGRAM_NOT_WITHIN_PARTICIPATION_TIME;

public class ProgramTimeWindowException extends CustomBadRequestException {
    public ProgramTimeWindowException() {
        super("해당 프로그램 참여 시간이 아닙니다.", PROGRAM_NOT_WITHIN_PARTICIPATION_TIME);
    }
}
