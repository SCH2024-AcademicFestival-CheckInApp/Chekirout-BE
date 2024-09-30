package com.sch.chekirout.program.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    private ProgramType programType;

    private Integer maxParticipationCount;

    private LocalDateTime startTimestamp;

    private LocalDateTime endTimestamp;

    private boolean notificationYn;

}
