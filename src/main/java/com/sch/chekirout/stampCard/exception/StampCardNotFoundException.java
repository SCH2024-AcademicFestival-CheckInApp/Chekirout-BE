package com.sch.chekirout.stampCard.exception;

import com.sch.chekirout.common.exception.CustomNotFoundException;

import static com.sch.chekirout.common.exception.ErrorCode.STAMP_CARD_NOT_FOUND;

public class StampCardNotFoundException  extends CustomNotFoundException {
    public StampCardNotFoundException(String studentId) {
        super("해당 스탬프카드가 없습니다. 학번: " + studentId, STAMP_CARD_NOT_FOUND);
    }
}
