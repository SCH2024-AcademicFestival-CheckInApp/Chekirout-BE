package com.sch.chekirout.prizeDraw.application.dto.response;


import com.sch.chekirout.prizeDraw.domain.Prize;
import com.sch.chekirout.prizeDraw.domain.PrizeWinner;
import com.sch.chekirout.user.domain.User;

import java.time.LocalDateTime;

public record PrizeWinnerResponse (
        Long PrizeWinnerId,
        String winnerName,
        String studentId,
        Long prizeId,
        String prizeName,
        LocalDateTime prizeClaimedAt

) {
    public static PrizeWinnerResponse from(PrizeWinner prizeWinner, User user, Prize prize) {
        return new PrizeWinnerResponse(
                prizeWinner.getId(),
                user.getName(),
                user.getUsername(),
                prizeWinner.getPrizeId(),
                prize.getPrizeName(),
                prizeWinner.getPrizeClaimedAt()
        );
    }
}