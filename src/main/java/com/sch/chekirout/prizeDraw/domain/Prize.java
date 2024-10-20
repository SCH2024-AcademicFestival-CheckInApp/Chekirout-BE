package com.sch.chekirout.prizeDraw.domain;

import com.sch.chekirout.common.domain.BaseEntity;
import com.sch.chekirout.prizeDraw.application.dto.request.PrizeRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prize extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prizeName;

    @Enumerated(EnumType.STRING)
    private PrizeClaimType prizeClaimType;

    public void update(PrizeRequest prizeRequest) {
        this.prizeName = prizeRequest.prizeName();
        this.prizeClaimType = prizeRequest.prizeClaimType();
    }

    public void delete() {
        this.setDeletedAt(LocalDateTime.now());
    }
}
