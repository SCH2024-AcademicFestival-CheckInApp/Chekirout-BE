package com.sch.chekirout.notification.FCMtoken.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FCMTokenRequest {

    private String email; // 사용자 이메일
    private String name;  // 사용자 이름
    private String token; // FCM 토큰
}

