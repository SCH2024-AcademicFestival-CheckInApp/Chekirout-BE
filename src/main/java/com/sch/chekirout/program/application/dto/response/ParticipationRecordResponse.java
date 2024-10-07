package com.sch.chekirout.program.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sch.chekirout.participation.domain.ParticipationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ParticipationRecordResponse (
        @Schema(description = "카테고리 이름")
        String categoryName,

        @Schema(description = "프로그램 이름")
        String programName,

        @Schema(description = "참여 시간")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        LocalDateTime participationTime,

        @Schema(description = "위도")
        double latitude,

        @Schema(description = "경도")
        double longitude
) {
    public static ParticipationRecordResponse from(ProgramResponse program, CategoryResponse category, ParticipationRecord participationRecord) {
        return new ParticipationRecordResponse(
                category.getName(),
                program.name(),
                participationRecord.getParticipationTime(),
                participationRecord.getLatitude(),
                participationRecord.getLongitude()
        );
    }
}
