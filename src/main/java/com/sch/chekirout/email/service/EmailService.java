package com.sch.chekirout.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "이메일 인증을 완료해주세요";
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;
        String message = "아래 링크를 클릭하여 이메일 인증을 완료하세요: " + verificationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}