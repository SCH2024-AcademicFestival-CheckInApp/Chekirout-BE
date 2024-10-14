package com.sch.chekirout.prizeDraw.application.dto.response;

import com.sch.chekirout.prizeDraw.domain.Prize;
import com.sch.chekirout.user.domain.Department;
import com.sch.chekirout.user.domain.User;

public record WinnerResponse(
        String studentId,
        Department department,
        String name,
        Long stampCardId
) {
    public static WinnerResponse from(User user, Prize prize) {
        return new WinnerResponse(
                user.getUsername(),
                user.getDepartment(),
                user.getName(),
                prize.getId()
        );
    }
}
