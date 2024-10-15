package com.sch.chekirout.prizeDraw.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.PRIZE_WINNER_ALREADY_CLAIMED;

public class AlreadyClaimedException extends CustomBadRequestException {
    public AlreadyClaimedException() {
        super(
                String.format("이미 경품을 수령했습니다."),
                PRIZE_WINNER_ALREADY_CLAIMED
        );
    }
}
