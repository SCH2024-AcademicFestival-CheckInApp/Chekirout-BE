package com.sch.chekirout.device.exception;

import com.sch.chekirout.common.exception.CustomBadRequestException;
import com.sch.chekirout.common.exception.ErrorCode;

public class DeviceNotFoundException extends CustomBadRequestException {
    public DeviceNotFoundException(String message) {
        super(message, ErrorCode.DEVICE_NOT_FOUND);
    }
}