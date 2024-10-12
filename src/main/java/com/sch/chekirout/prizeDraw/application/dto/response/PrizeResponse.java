package com.sch.chekirout.prizeDraw.application.dto.response;

import com.sch.chekirout.prizeDraw.domain.Prize;
import com.sch.chekirout.prizeDraw.domain.PrizeClaimType;

public record PrizeResponse (
        Long prizeId,
        String prizeName,
        PrizeClaimType prizeClaimType
){
    public static PrizeResponse from(Prize prize) {
        return new PrizeResponse(
                prize.getId(),
                prize.getPrizeName(),
                prize.getPrizeClaimType()
        );
    }
}
