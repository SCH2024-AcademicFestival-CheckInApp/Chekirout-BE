package com.sch.chekirout.program.domain.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class ProgramIdGenerator {

    public static String generateProgramId(Long categoryId, LocalDateTime startTimestamp) {

        // 1. 카테고리 ID를 활용한 PREFIX : 한 자리일 경우 0을 붙여서 표현
        String categoryPrefix = "C" + String.format("%02d", categoryId);  // 예: C01, C12, C51 등

        // 2. startTimestamp를 YYMMDD로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datePrefix = startTimestamp.format(formatter);  // 예: "241011"

        // 3. 랜덤한 문자열 또는 숫자 5자리 생성
        String randomSuffix = RandomStringUtils.randomAlphanumeric(5);  // 예: "A1B2C" (영숫자 혼합)

        // 4. 최종 ID 생성
        return categoryPrefix + "-" + datePrefix + "-" + randomSuffix;
    }
}
