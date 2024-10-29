package com.sch.chekirout.notification.FCMtoken.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fcm_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FCMToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    private String email; // 사용자 이메일 (users_info 테이블과 연동)

    private String name; // 사용자 이름

    private String token; // FCM 토큰

    //test
    public FCMToken(String email, String name, String token) {
        this.email = email;
        this.name = name;
        this.token = token;
    }
}
