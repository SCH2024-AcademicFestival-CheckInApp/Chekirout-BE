package com.sch.chekirout.user.dto.response;

import com.sch.chekirout.user.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private String deviceToken;
    private String deviceName;

    public static UserResponseDto from(User user, String deviceName) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .department(user.getDepartment())
                .role(user.getRole())
                .isNotificationEnabled(user.getIsNotificationEnabled())
                .phone(user.getPhone())
                .email(user.getEmail())
                .deviceToken(user.getDeviceToken())
                .deviceName(deviceName)
                .build();
    }
}