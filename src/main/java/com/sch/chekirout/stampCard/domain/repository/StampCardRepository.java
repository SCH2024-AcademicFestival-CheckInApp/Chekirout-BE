package com.sch.chekirout.stampCard.domain.repository;

import com.sch.chekirout.stampCard.domain.StampCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StampCardRepository extends JpaRepository<StampCard, Long> {
    Optional<StampCard> findByUserId(Long userId);
}
