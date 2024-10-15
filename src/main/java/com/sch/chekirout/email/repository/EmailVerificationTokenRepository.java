package com.sch.chekirout.email.repository;

import com.sch.chekirout.email.domain.EmailVerificationToken;
import com.sch.chekirout.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByUser(User user);  // 사용자 기반으로 토큰 조회 메서드 추가



}