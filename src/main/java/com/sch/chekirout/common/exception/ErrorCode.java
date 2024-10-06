package com.sch.chekirout.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 1000 - 1999: Category 관련 오류
    CATEGORY_NOT_FOUND(1000, "요청한 ID에 해당하는 카테고리가 존재하지 않습니다."),
    CATEGORY_ALREADY_EXIST(1001, "중복된 카테고리입니다."),

    // 2000 - 2999: Program 관련 오류
    PROGRAM_NOT_FOUND(2000, "요청한 ID에 해당하는 프로그램이 존재하지 않습니다."),

    // 3000 - 3999: StampCard 관련 오류
    STAMP_CARD_NOT_FOUND(3000, "요청한 사용자에 해당하는 스탬프 카드가 존재하지 않습니다."),
    STAMP_CARD_ALREADY_EXIST(3001, "스탬프 카드는 이미 존재합니다."),

    // 4000 - 4999: Participation 관련 오류
    ALREADY_PARTICIPATED(4000, "이미 참여한 프로그램입니다."),
    DISTANCE_OUT_OF_RANGE(4001, "참여 가능한 거리 범위를 벗어났습니다."),
    PROGRAM_NOT_WITHIN_PARTICIPATION_TIME(4002, "프로그램 참여 시간이 아닙니다."),
    INVALID_DATE_TIME_FORMAT(4003, "유효하지 않은 날짜 형식입니다."),

    // 9999: 서버 에러
    INTERNAL_SERVER_ERROR(9999, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}