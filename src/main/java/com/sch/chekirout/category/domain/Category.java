package com.sch.chekirout.category.domain;

import com.sch.chekirout.category.application.dto.request.CategoryRequest;
import com.sch.chekirout.common.domain.BaseEntity;
import com.sch.chekirout.program.domain.Program;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "programType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Program> programs;

    public void update(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
        this.description = categoryRequest.getDescription();
    }

}
