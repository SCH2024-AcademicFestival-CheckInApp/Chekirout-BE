package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.STUDENT_ID_ALREADY_EXISTS;

public class StudentIdAlreayExists extends CustomBadRequestException {
    public StudentIdAlreayExists() {
        super(
                STUDENT_ID_ALREADY_EXISTS.getMessage(),
                STUDENT_ID_ALREADY_EXISTS
        );
    }
}
