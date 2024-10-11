package com.sch.chekirout.stampCard.application.dto.response;

import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.user.domain.User;

import java.time.LocalDateTime;

public record StampCardResponse (
    String studentName,
    String studentId,
    int stampCount,
    LocalDateTime isCompleted
){
    public static StampCardResponse from(StampCard stampCard, User user) {
        return new StampCardResponse(
                user.getName(),
                user.getUsername(),
                stampCard.getStamps().size(),
                stampCard.getCompletedAt()
        );
    }
}