package com.sch.chekirout.program.application.dto.response;

import com.sch.chekirout.program.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryResponse {

    @Schema(description = "카테고리 id", nullable = false)
    private Long id;

    @Schema(description = "카테고리 이름", nullable = false)
    private String name;

    @Schema(description = "카테고리 설명")
    private String description;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
