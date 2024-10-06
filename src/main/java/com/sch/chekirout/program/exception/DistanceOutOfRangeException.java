package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

public class DistanceOutOfRangeException extends CustomBadRequestException {
    public DistanceOutOfRangeException() {
        super("거리 허용 범위를 벗어난 위치입니다.");
    }
}