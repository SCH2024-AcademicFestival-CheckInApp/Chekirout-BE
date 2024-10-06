package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

public class ProgramTimeWindowException extends CustomBadRequestException {
    public ProgramTimeWindowException() {
        super("해당 프로그램 참여 시간이 아닙니다.");
    }
}
