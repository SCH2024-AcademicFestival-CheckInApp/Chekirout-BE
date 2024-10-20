package com.sch.chekirout.program.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sch.chekirout.program.domain.Program;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record ProgramResponse(
        @Schema(description = "프로그램 ID")
        String id,

        @Schema(description = "프로그램 이름")
        String name,

        @Schema(description = "프로그램 설명")
        String description,

        @Schema(description = "카테고리 ID")
        Long categoryId,

        @Schema(description = "카테고리 이름")
        String categoryName,

        @Schema(description = "프로그램 시작 시간")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        LocalDateTime startTimestamp,

        @Schema(description = "프로그램 종료 시간")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        LocalDateTime endTimestamp,

        @Schema(description = "알림 여부")
        boolean notificationYn
) {

    public static ProgramResponse from(Program program) {
        return ProgramResponse.builder()
                .id(program.getId())
                .name(program.getName())
                .description(program.getDescription())
                .categoryId(program.getCategory().getId())
                .categoryName(program.getCategory().getName())
                .startTimestamp(program.getStartTimestamp())
                .endTimestamp(program.getEndTimestamp())
                .notificationYn(program.isNotificationYn())
                .build();
    }
}