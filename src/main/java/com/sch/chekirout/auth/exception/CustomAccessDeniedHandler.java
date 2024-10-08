package com.sch.chekirout.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sch.chekirout.global.presentation.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sch.chekirout.common.exception.ErrorCode.ADMIN_UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.error("관리자 권한이 없습니다.", accessDeniedException);
        log.error("Request Uri : {}", request.getRequestURI());

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = new ErrorResponse(
                ADMIN_UNAUTHORIZED.getMessage(),
                ADMIN_UNAUTHORIZED,
                HttpStatus.FORBIDDEN.value() // 403 Forbidden
        );

        // JSON으로 변환
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        // 응답 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }


}
