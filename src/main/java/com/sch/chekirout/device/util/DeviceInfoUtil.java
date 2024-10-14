package com.sch.chekirout.device.util;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceInfoUtil {

    public static String extractDeviceName(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent.split(" ")[0] : "Unknown Device";
    }

    public static String extractOperatingSystem(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "Mac OS";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone")) {
            return "iOS";
        }
        return "Unknown OS";
    }

    public static String extractBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        }
        return "Unknown Browser";
    }

    public static String extractIpAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public static String extractUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}