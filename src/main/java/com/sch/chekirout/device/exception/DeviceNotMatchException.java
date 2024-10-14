package com.sch.chekirout.device.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;

public class DeviceNotMatchException extends CustomBadRequestException {

    public DeviceNotMatchException() {
        super("다른 기기에서 로그인 시도가 감지되었습니다.", ErrorCode.DEVICE_NOT_MATCH);
    }
}