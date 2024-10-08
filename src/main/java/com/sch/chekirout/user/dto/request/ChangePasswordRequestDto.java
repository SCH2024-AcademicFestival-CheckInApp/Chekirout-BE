package com.sch.chekirout.user.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
public class ChangePasswordRequestDto {
    private String currentPassword;  // 현재 비밀번호
    private String newPassword;      // 새로운 비밀번호

    public ChangePasswordRequestDto() {
    }


    public ChangePasswordRequestDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
