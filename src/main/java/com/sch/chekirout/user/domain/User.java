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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키 ID는 그대로 유지

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자여야 합니다.")  // 8자리 숫자 형식 유효성 검증
    private String username;  // 학번으로 대체

    @Column(nullable = false)
    private String department;

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
        if (prizeStatus == null) {
            this.prizeStatus = UserPrizeStatus.NOT_ELIGIBLE;
        }
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_participation_counts", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "program_id")
    @Column(name = "participation_count")
    private Map<UUID, Integer> participationCounts = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_program_type_counts", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "program_type")
    @Column(name = "program_type_count")
    private Map<String, Integer> programTypeCounts = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_program_history", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "program_name")
    private List<String> programHistory = new ArrayList<>();

    //private Boolean isEligibleForPrize = false;
    private LocalDateTime prizeEligibilityTimestamp;  // 경품 자격 획득 시점

    //private Boolean isWinner = false;
    @Enumerated(EnumType.STRING)
    private UserPrizeStatus prizeStatus = UserPrizeStatus.NOT_ELIGIBLE;

    //private Boolean isNotificationEnabled = true;
    private LocalDateTime notificationEnabledAt;  // 알림 활성화 시점


    // 기본 생성자
    public User() {
    }

    // 필드 값을 설정할 수 있는 생성자 추가
    public User(String username, String department, String name, String password, UserRole role) {
        this.username = username;
        this.department = department;
        this.name = name;
        this.password = password;
        this.role = role != null ? role : UserRole.STUDENT;  // Role이 null이면 STUDENT로 설정
    }


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


    // ================= 상태 변경 메서드들 ==================



    public void enablePrizeEligibility() {
        this.prizeEligibilityTimestamp = LocalDateTime.now();
        this.prizeStatus = UserPrizeStatus.ELIGIBLE;
    }

    public void markAsWinner() {
        this.prizeStatus = UserPrizeStatus.WINNER;
    }

    public void enableNotification() {
        this.notificationEnabledAt = LocalDateTime.now();
    }

    public void disableNotification() {
        this.notificationEnabledAt = null;
    }


}

