package com.sch.chekirout.email.controller;


import com.sch.chekirout.email.service.EmailService;
import com.sch.chekirout.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok("이메일 인증이 완료되었습니다. 회원가입을 진행해주세요!!");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않거나 만료된 인증 토큰입니다.");
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