package com.sch.chekirout.user.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Getter
@Entity
@Table(name = "users_Info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키 ID는 그대로 유지

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자여야 합니다.")  // 8자리 숫자 형식 유효성 검증
    private String username;  // 학번으로 대체

    @Column(nullable = false)
    private Department department;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.STUDENT;

    @PrePersist
    public void prePersist(){
        if(role == null){
            this.role=UserRole.STUDENT;
        }
    }

    // 연관 관계 설정: 분리된 엔티티와의 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserParticipation> participationCounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserProgramType> programTypeCounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserProgramHistory> programHistory;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserPrizeInfo prizeInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserNotification notification;


    // 기본 생성자
    public User() {
    }

    // 필드 값을 설정할 수 있는 생성자 추가
    public User(String username, Department department, String name, String password, UserRole role) {
        this.username = username;
        this.department = department;
        this.name = name;
        this.password = password;
        this.role = role != null ? role : UserRole.STUDENT;  // Role이 null이면 STUDENT로 설정
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

