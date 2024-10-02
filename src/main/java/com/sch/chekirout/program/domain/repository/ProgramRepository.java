package com.sch.chekirout.program.domain.repository;

import com.sch.chekirout.program.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findAllByDeletedAtIsNullOrderByStartTimestamp();

    Optional<Program> findById(String id);
}
