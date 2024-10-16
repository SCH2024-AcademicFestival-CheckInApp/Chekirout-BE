package com.sch.chekirout.email.service;

import com.sch.chekirout.email.domain.EmailVerificationToken;
import com.sch.chekirout.email.repository.EmailVerificationTokenRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.exception.TokenNotFoundException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final EmailVerificationTokenRepository tokenRepository;

    public void sendVerificationEmail(String recipientEmail) {

        String token = generateEmailVerificationToken(recipientEmail);

        String subject = "이메일 인증을 완료해주세요";
        String verificationUrl = "https://dev.chekirout.com/api/v1/auth/verify-email?token=" + token;
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

    private String generateEmailVerificationToken(String email) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, email);
        tokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public boolean verifyEmail(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    verificationToken.activateEmail();
                    tokenRepository.save(verificationToken);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean isActive(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    verificationToken.isActive();
                    return true;
                })
                .orElse(false);
    }

    public boolean deleteToken(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException());

        if(verificationToken.isActive()) {
            throw new TokenNotFoundException();
        }

        tokenRepository.delete(verificationToken);

        return true;
    }
}
