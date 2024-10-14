package com.sch.chekirout.prizeDraw.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;

import static com.sch.chekirout.common.exception.ErrorCode.NOT_ENOUGH_ELIGIBLE_PARTICIPANTS;

public class NotEnoughEligibleParticipantsException extends CustomBadRequestException {
    public NotEnoughEligibleParticipantsException(int numberOfParticipants) {
        super(
                String.format("추첨 대상자가 부족합니다. 현재 대상자 수 : %d", numberOfParticipants),
                NOT_ENOUGH_ELIGIBLE_PARTICIPANTS
        );
    }
}
