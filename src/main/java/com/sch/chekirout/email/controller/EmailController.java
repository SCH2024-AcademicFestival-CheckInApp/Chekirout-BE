package com.sch.chekirout.email.controller;


import com.sch.chekirout.email.service.EmailService;
import com.sch.chekirout.user.application.UserService;
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

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = emailService.verifyEmail(token);

        if (isVerified) {
            return ResponseEntity.ok("이메일 인증이 완료되었습니다. 회원가입이 최종 완료되었습니다. 로그인을 진행해주세요!!");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않거나 만료된 인증 토큰입니다.");
        }
    }


//    @PostMapping("/resend-verification")
//    public ResponseEntity<String> resendVerificationToken(@RequestParam String email) {
//        userService.resendVerificationToken(email);
//        return ResponseEntity.ok("새로운 인증 토큰이 이메일로 전송되었습니다.");
//    }
}