package com.sch.chekirout.email.controller;


import com.sch.chekirout.email.service.EmailService;
import com.sch.chekirout.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/checkEmail")
    public void checkEmail(@RequestParam String email) {

        // 이메일 인증 토큰 생성 및 이메일 발송
        emailService.sendVerificationEmail(email);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = emailService.verifyEmail(token);

        if (isVerified) {
            // 인증 성공 시 리다이렉트
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("https://dev.chekirout.com/email/verifyToken"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);  // 302 리다이렉트
        } else {
            // 인증 실패 시 적절한 에러 메시지와 400 Bad Request 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유효하지 않거나 만료된 인증 토큰입니다.");
        }
    }


    @Operation(
            summary = "이메일 유효성 검사",
            description = "이메일 형식 및 중복 검사 API"
    )
    @GetMapping("/validate-email")
    public ResponseEntity<String> validateEmail(@RequestParam String email) {
        // 이메일 형식 검증 (학교 이메일 형식인지 확인)
        if (!email.matches("^[A-Za-z0-9._%+-]+@sch\\.ac\\.kr$")) {
            return ResponseEntity.badRequest().body("학교 형식 이메일을 사용해야합니다.");
        }

        // 이메일 중복 검사
        if (emailService.isEmailAlreadyRegistered(email)) {
            return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
        }

        return ResponseEntity.ok().body("사용 가능한 이메일입니다.");
    }
}