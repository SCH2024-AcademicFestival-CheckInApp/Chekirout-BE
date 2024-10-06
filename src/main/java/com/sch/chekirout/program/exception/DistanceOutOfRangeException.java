package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.Errorcode.DISTANCE_OUT_OF_RANGE;

public class DistanceOutOfRangeException extends CustomBadRequestException {
    public DistanceOutOfRangeException() {
        super(
                "거리 허용 범위를 벗어난 위치입니다.",
                DISTANCE_OUT_OF_RANGE
        );
    }
}