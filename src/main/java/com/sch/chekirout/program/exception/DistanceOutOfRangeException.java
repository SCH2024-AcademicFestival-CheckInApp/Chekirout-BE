package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.DISTANCE_OUT_OF_RANGE;

public class DistanceOutOfRangeException extends CustomBadRequestException {
    public DistanceOutOfRangeException() {
        super(
                DISTANCE_OUT_OF_RANGE.getMessage(),
                DISTANCE_OUT_OF_RANGE
        );
    }
}