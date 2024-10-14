package com.sch.chekirout.device.util;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UserAgentUtil {
    public static String extractDeviceInfo(String userAgent) {
        // 정규식으로 괄호 안의 내용 추출
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            return matcher.group(1);  // 괄호 안의 내용 반환
        }
        return null;
    }
}