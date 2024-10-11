package com.sch.chekirout.user.dto.request;

import com.sch.chekirout.user.domain.Department;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.domain.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotEmpty(message = "학번(username)은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[0-9]{8}$", message = "학번(username)은 정수로 된 8자리 숫자여야 합니다.")  // 학번 형식 검증
    private String username;

    @NotNull(message = "학과는 필수 입력 항목입니다.")
    private Department department;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    private String email;

    private String phone;

    public User toEntity(String passwordEncoded) {
        return User.builder()
                .username(username)
                .department(department)
                .name(name)
                .password(passwordEncoded)
                .email(email)
                .phone(phone)
                .role(UserRole.STUDENT)
                .build();
    }
}
