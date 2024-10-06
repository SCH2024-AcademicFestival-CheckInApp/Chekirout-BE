package com.sch.chekirout.stampCard.exception;

public class StampCardNotFoundException  extends RuntimeException {
    public StampCardNotFoundException(Long userId) {
        super("StampCard not found for userId: " + userId);
    }
}
