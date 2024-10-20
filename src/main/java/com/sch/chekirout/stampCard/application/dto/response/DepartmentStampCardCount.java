package com.sch.chekirout.stampCard.application.dto.response;

import com.sch.chekirout.user.domain.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentStampCardCount {
    private Department department;
    private Long stampCardCount;
}