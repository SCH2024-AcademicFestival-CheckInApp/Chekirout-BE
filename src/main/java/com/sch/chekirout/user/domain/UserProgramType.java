package com.sch.chekirout.user.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_program_type")
public class UserProgramType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String programType;  // 프로그램 타입

    @Column(nullable = false)
    private Integer count;  // 타입별 참여 횟수
}