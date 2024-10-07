package com.sch.chekirout.program.application.dto.response;

import com.sch.chekirout.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ProgramParticipationHistories (

        @Schema(description = "이름")
        String studentName,

        @Schema(description = "학번")
        String studentId,

        @Schema(description = "참여 기록")
        List<ParticipationRecordResponse> participationRecords
) {
    public static ProgramParticipationHistories from(User user, List<ParticipationRecordResponse> participationRecords) {
        return new ProgramParticipationHistories(
                user.getName(),
                user.getUsername(),
                participationRecords
        );
    }
}
