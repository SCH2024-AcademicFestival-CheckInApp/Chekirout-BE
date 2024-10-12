package com.sch.chekirout.prizeDraw.exception;

import com.sch.chekirout.common.exception.CustomNotFoundException;

import static com.sch.chekirout.common.exception.ErrorCode.*;

public class PrizeNotFoundException extends CustomNotFoundException {

    public PrizeNotFoundException(final Long prizeId) {
        super(
                String.format("상품을 찾을 수 없습니다. 상품 아이디 : %d", prizeId),
                PRIZE_NOT_FOUND);
    }
}
