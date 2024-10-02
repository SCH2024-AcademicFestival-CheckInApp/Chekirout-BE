package com.sch.chekirout.category.application.dto.request;

import com.sch.chekirout.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank
    @Schema(description = "카테고리 이름", nullable = false)
    private final String name;

    @NotBlank
    @Schema(description = "카테고리 설명")
    private final String description;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }
}
