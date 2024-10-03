package com.sch.chekirout.user.dto.request;

import lombok.Getter;
import lombok.Setter;

// 필요한 필드만 포함하는 DTO 클래스
@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String department;
    private String name;
    private Boolean isEligibleForPrize;
    private Boolean isWinner;
    private Boolean isNotificationEnabled;


    public UserResponseDto(String username, String department, String name, Boolean isEligibleForPrize, Boolean isWinner, Boolean isNotificationEnabled) {
        this.username = username;
        this.department = department;
        this.name = name;
        this.isEligibleForPrize = isEligibleForPrize;
        this.isWinner = isWinner;
        this.isNotificationEnabled = isNotificationEnabled;
    }
}