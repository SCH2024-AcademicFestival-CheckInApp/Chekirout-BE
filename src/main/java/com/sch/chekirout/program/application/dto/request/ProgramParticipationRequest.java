package com.sch.chekirout.program.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
//@AllArgsConstructor
public class ProgramParticipationRequest {

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private LocalDateTime timestamp;
}
