package com.sch.chekirout.program.domain;

import com.sch.chekirout.program.application.dto.request.CategoryRequest;
import com.sch.chekirout.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Program> programs;

    public void update(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
        this.description = categoryRequest.getDescription();
    }

    public void delete() {
        this.setDeletedAt(LocalDateTime.now());
    }
}
