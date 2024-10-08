package com.sch.chekirout.user.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "user_participation")
public class UserParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private UUID programId;  // 참여 프로그램 ID

    @Column(nullable = false)
    private Integer count;  // 참여 횟수
}
