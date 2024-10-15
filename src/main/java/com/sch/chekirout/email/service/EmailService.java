package com.sch.chekirout.email.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "이메일 인증을 완료해주세요";
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;
        String message = "아래 링크를 클릭하여 이메일 인증을 완료하세요: <a href='" + verificationUrl + "'>인증 링크</a>";

        try {
            // MimeMessage를 사용하여 이메일 생성
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

            helper.setTo(recipientEmail);  // 수신자 이메일 설정
            helper.setSubject(subject);    // 이메일 제목 설정
            helper.setText(message, true); // HTML 형식으로 본문 설정
            helper.setFrom("chekirout <juheun9912@gmail.com>");

            mailSender.send(email);  // 이메일 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
