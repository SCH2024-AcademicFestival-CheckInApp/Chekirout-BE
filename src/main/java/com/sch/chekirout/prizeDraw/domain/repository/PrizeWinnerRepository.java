package com.sch.chekirout.prizeDraw.domain.repository;

import com.sch.chekirout.prizeDraw.domain.PrizeWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrizeWinnerRepository extends JpaRepository<PrizeWinner, Long> {
}
