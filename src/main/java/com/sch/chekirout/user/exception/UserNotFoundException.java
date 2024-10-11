package com.sch.chekirout.user.exception;

import com.sch.chekirout.common.exception.CustomNotFoundException;
import com.sch.chekirout.common.exception.ErrorCode;

public class UserNotFoundException extends CustomNotFoundException {
    public UserNotFoundException(String username) {
        super(
                "해당 학번의 사용자를 찾을 수 없습니다. 학번: " + username,
                ErrorCode.USER_NOT_FOUND
        );
    }
}
