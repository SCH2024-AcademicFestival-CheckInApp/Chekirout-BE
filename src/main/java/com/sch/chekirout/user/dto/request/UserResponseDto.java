package com.sch.chekirout.user.dto.request;

import com.sch.chekirout.user.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


// 필요한 필드만 포함하는 DTO 클래스
@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String name;
    private Department department;
    private UserRole role;
    private LocalDateTime isNotificationEnabled;
    private String phone;
    private String email;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .department(user.getDepartment())
                .role(user.getRole())
                .isNotificationEnabled(user.getIsNotificationEnabled())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}