package com.sch.chekirout.stampCard.application.dto.response;

import com.sch.chekirout.user.domain.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentTotalStampCount {
    private Department department;
    private Long totalStamps;       // 학과별 총 스탬프 개수
}