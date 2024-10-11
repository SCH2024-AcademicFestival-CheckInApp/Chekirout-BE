package com.sch.chekirout.user.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_Info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키 ID는 그대로 유지

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자여야 합니다.")  // 8자리 숫자 형식 유효성 검증
    private String username;  // 학번으로 대체

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.STUDENT;

    private LocalDateTime isNotificationEnabled;

    @PrePersist
    public void prePersist(){
        if(role == null){
            this.role=UserRole.STUDENT;
        }
    }

    // 상태 변경 메서드들
    public void updateRole(UserRole newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        this.role = newRole;
    }

    public void updatePassword(String newPassword, PasswordEncoder passwordEncoder) {
        if (newPassword == null || newPassword.trim().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        this.password = passwordEncoder.encode(newPassword);  // 새로운 비밀번호 설정
    }
}

