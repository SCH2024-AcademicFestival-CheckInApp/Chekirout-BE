package com.sch.chekirout.prizeDraw.domain.repository;

import com.sch.chekirout.prizeDraw.domain.Prize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Long> {

    List<Prize> findAllByDeletedAtIsNull();
}
