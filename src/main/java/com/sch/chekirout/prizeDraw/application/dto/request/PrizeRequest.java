package com.sch.chekirout.prizeDraw.application.dto.request;

import com.sch.chekirout.prizeDraw.domain.Prize;
import com.sch.chekirout.prizeDraw.domain.PrizeClaimType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrizeRequest (

        @NotBlank
        @Schema(description = "경품 이름", nullable = false)
        String prizeName,

        @NotNull
        @Schema(description = "", nullable = false)
        PrizeClaimType prizeClaimType
) {
    public Prize toEntity() {
        return Prize.builder()
                .prizeName(prizeName)
                .prizeClaimType(prizeClaimType)
                .build();
    }
}
