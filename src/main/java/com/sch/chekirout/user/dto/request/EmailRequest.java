package com.sch.chekirout.user.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@sch\\.ac\\.kr$", message = "학교 이메일 형식만 사용 가능합니다.")
    private String email;
}