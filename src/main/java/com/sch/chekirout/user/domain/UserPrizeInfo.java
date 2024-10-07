package com.sch.chekirout.user.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_prize_info")
public class UserPrizeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime prizeEligibilityTimestamp;  // 경품 자격 획득 시점
    private LocalDateTime winnerTimestamp;  // 당첨 시점
    private UserPrizeStatus prizeStatus = UserPrizeStatus.NOT_ELIGIBLE;  // 경품 상태
}