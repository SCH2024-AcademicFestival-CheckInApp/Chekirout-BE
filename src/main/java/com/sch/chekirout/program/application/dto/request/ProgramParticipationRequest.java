package com.sch.chekirout.program.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
//@AllArgsConstructor
public class ProgramParticipationRequest {

    @NotNull
    @Schema(description = "위도")
    private double latitude;

    @NotNull
    @Schema(description = "경도")
    private double longitude;

    @NotNull
    @Schema(description = "타임스탬프 - 요청 시간", example = "2024-10-02T10:00:00")
    private LocalDateTime timestamp;
}
