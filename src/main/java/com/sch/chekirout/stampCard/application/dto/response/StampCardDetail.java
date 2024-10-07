package com.sch.chekirout.stampCard.application.dto.response;

import com.sch.chekirout.stampCard.domain.Stamp;
import com.sch.chekirout.stampCard.domain.StampCard;

import java.time.LocalDateTime;
import java.util.List;

public record StampCardDetail(String studentId, int stampCount, List<Stamp> stamps,
                              LocalDateTime isCompleted) {

    public static StampCardDetail from(StampCard stampCard, String studentId) {
        return new StampCardDetail(
                studentId,
                stampCard.getStamps().size(),
                stampCard.getStamps(),
                stampCard.getCompletedAt()
        );
    }
}