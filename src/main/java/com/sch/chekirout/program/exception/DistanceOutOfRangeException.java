package com.sch.chekirout.program.exception;

public class DistanceOutOfRangeException extends RuntimeException {
    public DistanceOutOfRangeException() {
        super("거리 허용 범위를 벗어난 위치입니다.");
    }
}