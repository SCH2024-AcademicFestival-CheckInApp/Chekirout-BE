package com.sch.chekirout.stampCard.exception;

import com.sch.chekirout.common.exception.CustomNotFoundException;

import static com.sch.chekirout.common.exception.Errorcode.PROGRAM_NOT_FOUND;

public class StampCardNotFoundException  extends CustomNotFoundException {
    public StampCardNotFoundException(Long userId) {
        super("StampCard not found for userId: " + userId, PROGRAM_NOT_FOUND);
    }
}
