package com.sch.chekirout.stampCard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StampCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "stamp", joinColumns = @JoinColumn(name = "stamp_card_id"))
    @OrderColumn(name = "line_idx")
    private List<Stamp> stamps;

    private LocalDateTime completedAt;

    public static StampCard createNewStampCard(Long userId) {
        return StampCard.builder()
                .userId(userId)
                .stamps(new ArrayList<>())
                .build();
    }

    // 해당 카테고리 스탬프가 있는지 확인
    public boolean hasStampForCategory(Long categoryId) {
        return stamps.stream()
                .anyMatch(stamp -> stamp.getCategoryId().equals(categoryId));
    }

    public void addStamp(Stamp stamp, List<Long> requiredCategoryIds) {
        stamps.add(stamp);
        checkIfCompleted(requiredCategoryIds);
    }

    private void checkIfCompleted(List<Long> requiredCategoryIds) {
        // 필수 카테고리 목록 (예시: 필수 카테고리 ID 목록)
        boolean isCompleted = requiredCategoryIds.stream()
                .allMatch(this::hasStampForCategory);

        // 완료 여부에 따라 스탬프 카드를 완료 처리
        markAsCompletedIfRequired(isCompleted);
    }

    private void markAsCompletedIfRequired(boolean isCompleted) {
        if (isCompleted) {
            this.completedAt = LocalDateTime.now();
        }
    }
}
