package com.sch.chekirout.stampCard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stamp {

    private Long categoryId;

    private String programId;

    private String categoryName;

    private String programName;

    private LocalDateTime timestamp;
}
