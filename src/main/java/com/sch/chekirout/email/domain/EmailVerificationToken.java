package com.sch.chekirout.email.domain;


import com.sch.chekirout.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    private LocalDateTime expiryDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status = EmailStatus.PENDING;  // 초기 상태는 PENDING

    // 기본 생성자 (필수)
    public EmailVerificationToken() {
    }

    public EmailVerificationToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expiryDate = LocalDateTime.now().plusMinutes(5);  // 유효시간 5분
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }


    public void activateEmail() {
        this.status = EmailStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == EmailStatus.ACTIVE;
    }

}