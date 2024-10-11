package com.sch.chekirout.stampCard.domain.repository;

import com.sch.chekirout.stampCard.domain.StampCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StampCardRepository extends JpaRepository<StampCard, Long> {

    Optional<StampCard> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Long countAllBy();

    Long countByCompletedAtIsNotNull();

    @Query("SELECT sc FROM StampCard sc " +
            "ORDER BY SIZE(sc.stamps) ASC, sc.completedAt ASC")
    Page<StampCard> findAllOrderByStampsSizeAndCompletedAt(Pageable pageable);

    Page<StampCard> findAllByCompletedAtIsNotNull(Pageable pageable);
}
