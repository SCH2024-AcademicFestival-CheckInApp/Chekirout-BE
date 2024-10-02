package com.sch.chekirout.program.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateProgramRequest {

    @Schema(description = "프로그램 타입(카테고리) id")
    private Long programTypeId;

    @Schema(description = "프로그램 이름")
    private String name;

    @Schema(description = "프로그램 시작 시간")
    private String startTimestamp;

    @Schema(description = "프로그램 종료 시간")
    private String endTimestamp;

    @Schema(description = "알림 여부")
    private boolean notificationYn;
}
