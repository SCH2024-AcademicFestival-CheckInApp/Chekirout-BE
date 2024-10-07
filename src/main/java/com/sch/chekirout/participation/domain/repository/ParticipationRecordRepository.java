package com.sch.chekirout.participation.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sch.chekirout.participation.domain.ParticipationRecord;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRecordRepository extends JpaRepository<ParticipationRecord, Long> {

    List<ParticipationRecord> findAllByUserId(Long userId);

    Optional<ParticipationRecord> findByUserIdAndProgramId(Long userId, String programId);

    void deleteByUserIdAndProgramId(Long userId, String programId);
}
