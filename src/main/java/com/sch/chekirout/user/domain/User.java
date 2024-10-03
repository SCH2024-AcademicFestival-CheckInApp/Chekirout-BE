package com.sch.chekirout.user.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
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
    private UserRole role;

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

    private Boolean isEligibleForPrize = false;

    private Boolean isWinner = false;

    private Boolean isNotificationEnabled = true;

}

