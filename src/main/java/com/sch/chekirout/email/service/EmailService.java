package com.sch.chekirout.email.service;

import com.sch.chekirout.email.domain.EmailVerificationToken;
import com.sch.chekirout.email.repository.EmailVerificationTokenRepository;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.exception.TokenNotExpiredException;
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
    private final UserRepository userRepository;

    public static void resendVerificationToken(String email) {
    }

    public void sendVerificationEmail(String recipientEmail) {

        String token = generateEmailVerificationToken(recipientEmail);

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
            helper.setFrom("chekirout");

            mailSender.send(email);  // 이메일 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateEmailVerificationToken(String email) {

        // 이미 존재하는 토큰 확인
        EmailVerificationToken existingToken = tokenRepository.findByEmail(email);

        // 이메일이 존재하고, 토큰이 만료되었으면 삭제
        if (existingToken != null) {
            if (existingToken.isExpired()) {
                tokenRepository.delete(existingToken);  // 만료된 토큰 삭제
            } else {
                // 만료되지 않은 경우 예외 발생
                throw new TokenNotExpiredException();
            }
        }

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

        tokenRepository.delete(verificationToken);

        return true;
    }


    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}
