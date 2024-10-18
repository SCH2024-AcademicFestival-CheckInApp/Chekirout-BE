package com.sch.chekirout.email.service;

import com.sch.chekirout.email.domain.EmailVerificationToken;
import com.sch.chekirout.email.repository.EmailVerificationTokenRepository;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import com.sch.chekirout.user.exception.TokenExpiredException;
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
    
    public void sendVerificationEmail(String recipientEmail) {

        String token = generateEmailVerificationToken(recipientEmail);

        String subject = "이메일 인증을 완료해주세요";
        String verificationUrl = "http://dev.chekirout.com/api/v1/auth/verify-email?token=" + token;
        String message = "아래 링크를 클릭하여 이메일 인증을 완료하세요: <a href='" + verificationUrl + "'>인증 링크</a>";

        try {
            // MimeMessage를 사용하여 이메일 생성
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

            helper.setTo(recipientEmail);  // 수신자 이메일 설정
            helper.setSubject(subject);    // 이메일 제목 설정
            helper.setText(message, true); // HTML 형식으로 본문 설정
            helper.setFrom("chekirout <juheun9912@gamil.com>");

            mailSender.send(email);  // 이메일 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateEmailVerificationToken(String email) {

        // 이미 존재하는 토큰 확인
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findFirstByEmailOrderByExpiryDateDesc(email);
        optionalToken.ifPresent(existingToken -> {
            if (!existingToken.isExpired()) {
                throw new TokenNotExpiredException();
            }
        });

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, email);
        tokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public boolean verifyEmail(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    if (verificationToken.isExpired()) {
                        throw new TokenExpiredException();
                    }
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
                    if(verificationToken.isActive()){
                    tokenRepository.save(verificationToken);
                    return true;}
                    return false;
                })
                .orElse(false);
    }

    public boolean isExpired(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    verificationToken.isExpired();
                    return true;
                })
                .orElse(false);
    }


    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}
