package com.sch.chekirout.prizeDraw.application.dto.response;

import java.util.List;

public record DrawResult(
        Long prizeId,
        String prizeName,
        Integer numberOfWinners,
        List<WinnerResponse> winners

) {
}

