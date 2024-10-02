package com.sch.chekirout.program.application;

import com.sch.chekirout.category.domain.Category;
import com.sch.chekirout.category.domain.repository.CategoryRepository;
import com.sch.chekirout.category.exception.CategoryNotFoundException;
import com.sch.chekirout.program.application.dto.request.ProgramRegisterRequest;
import com.sch.chekirout.program.application.dto.request.ProgramUpdateRequest;
import com.sch.chekirout.program.application.dto.response.ProgramResponse;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import com.sch.chekirout.program.exception.ProgramNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public String saveProgram(ProgramRegisterRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("존재하지 않는 카테고리입니다."));

        return programRepository.save(request.toEntity(category)).getId();
    }

    @Transactional(readOnly = true)
    public List<ProgramResponse> getPrograms() {
        return programRepository.findAllByDeletedAtIsNullOrderByStartTimestamp().stream()
                .map(ProgramResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProgramResponse getProgram(String id) {
        return programRepository.findById(id)
                .map(ProgramResponse::from)
                .orElseThrow(() -> new ProgramNotFoundException(id));
    }

    @Transactional
    public void updateProgram(String id, ProgramUpdateRequest request) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));

        program.update(request);
    }

    @Transactional
    public void deleteProgram(String id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));

        program.delete();
    }
}
