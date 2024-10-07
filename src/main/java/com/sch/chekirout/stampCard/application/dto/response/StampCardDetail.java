package com.sch.chekirout.stampCard.application.dto.response;

import com.sch.chekirout.stampCard.domain.Stamp;
import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public record StampCardDetail(
        String studentName,
        String studentId,
        int stampCount,
        List<Stamp> stamps,
        LocalDateTime isCompleted) {

    public static StampCardDetail from(StampCard stampCard, User user) {
        return new StampCardDetail(
                user.getName(),
                user.getUsername(),
                stampCard.getStamps().size(),
                stampCard.getStamps(),
                stampCard.getCompletedAt()
        );
    }
}