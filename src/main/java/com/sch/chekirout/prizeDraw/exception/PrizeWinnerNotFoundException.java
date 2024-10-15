package com.sch.chekirout.prizeDraw.exception;

import com.sch.chekirout.common.exception.CustomNotFoundException;

import static com.sch.chekirout.common.exception.ErrorCode.PRIZE_WINNER_NOT_FOUND;

public class PrizeWinnerNotFoundException extends CustomNotFoundException {

    public PrizeWinnerNotFoundException(final String studentId) {
        super(
                String.format("경품 당첨자를 찾을 수 없습니다. 학번: : %s", studentId),
                PRIZE_WINNER_NOT_FOUND);
    }
}
