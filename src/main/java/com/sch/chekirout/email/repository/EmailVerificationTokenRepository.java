package com.sch.chekirout.email.repository;

import com.sch.chekirout.email.domain.EmailVerificationToken;
import com.sch.chekirout.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    @Query(value = "SELECT * FROM email_verification_token WHERE email = :email ORDER BY expiry_date DESC LIMIT 1", nativeQuery = true)
    Optional<EmailVerificationToken> findFirstByEmailOrderByExpiryDateDesc(@Param("email") String email);

}