package com.sch.chekirout.program.application.dto.request;

import com.sch.chekirout.program.domain.Category;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.util.ProgramIdGenerator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProgramRegisterRequest {

    @Positive
    @Schema(description = "카테고리 id")
    private Long categoryId;

    @NotBlank
    @Schema(description = "프로그램 이름")
    private String name;

    @NotBlank
    @Schema(description = "프로그램 설명")
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "프로그램 시작 시간", example = "2024-10-02T10:00:00")
    private LocalDateTime startTimestamp;

    @NotNull
    @Schema(description = "프로그램 종료 시간", example = "2024-10-02T10:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTimestamp;

    @Schema(description = "알림 여부")
    private boolean notificationYn;

    public Program toEntity(Category category) {
        return Program.builder()
                .id(ProgramIdGenerator.generateOrderId(category.getId(), startTimestamp))
                .name(name)
                .description(description)
                .category(category)
                .startTimestamp(startTimestamp)
                .endTimestamp(endTimestamp)
                .notificationYn(notificationYn)
                .build();
    }
}
