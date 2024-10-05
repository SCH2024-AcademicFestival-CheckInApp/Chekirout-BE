package com.sch.chekirout.program.domain;

import com.sch.chekirout.common.domain.BaseEntity;
import com.sch.chekirout.admin.application.dto.request.ProgramUpdateRequest;
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
public class Program extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime startTimestamp;

    @Column(nullable = false)
    private LocalDateTime endTimestamp;

    private boolean notificationYn;

    public void update(ProgramUpdateRequest programUpdateRequest) {
        this.name = programUpdateRequest.getName();
        this.description = programUpdateRequest.getDescription();
        this.startTimestamp = programUpdateRequest.getStartTimestamp();
        this.endTimestamp = programUpdateRequest.getEndTimestamp();
        this.notificationYn = programUpdateRequest.isNotificationYn();
    }

    public void delete() {
        this.setDeletedAt(LocalDateTime.now());
    }
}
