package com.sch.chekirout.notification.FCMtoken.domain.Repository;

import com.sch.chekirout.notification.FCMtoken.domain.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    Optional<FCMToken> findByEmail(String email);

    void deleteByEmail(String email);
}

