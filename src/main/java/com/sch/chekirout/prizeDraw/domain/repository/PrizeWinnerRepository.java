package com.sch.chekirout.prizeDraw.domain.repository;

import com.sch.chekirout.prizeDraw.domain.PrizeWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrizeWinnerRepository extends JpaRepository<PrizeWinner, Long> {
    List<PrizeWinner> findAllByPrizeId(Long prizeId);

    Optional<PrizeWinner> findByUserId(Long id);

    List<PrizeWinner> findAllByUserId(Long id);
}
