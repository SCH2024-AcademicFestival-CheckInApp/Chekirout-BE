package com.sch.chekirout.program.application;

import com.sch.chekirout.notification.NotificationScheduler;
import com.sch.chekirout.program.domain.Category;
import com.sch.chekirout.program.domain.repository.CategoryRepository;
import com.sch.chekirout.program.exception.CategoryNotFoundException;
import com.sch.chekirout.program.application.dto.request.ProgramRegisterRequest;
import com.sch.chekirout.program.application.dto.request.ProgramUpdateRequest;
import com.sch.chekirout.program.application.dto.response.ProgramResponse;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import com.sch.chekirout.program.exception.ProgramNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationScheduler notificationScheduler;

    @Transactional
    public String saveProgram(ProgramRegisterRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("존재하지 않는 카테고리입니다."));

        // 저장된 프로그램 객체를 가져옴
        Program savedProgram = programRepository.save(request.toEntity(category));

        // 알림 예약
        notificationScheduler.scheduleNotification(savedProgram);

        return savedProgram.getId();
    }

    @Transactional(readOnly = true)
    public List<ProgramResponse> getPrograms() {
        return programRepository.findAllByDeletedAtIsNullOrderByStartTimestamp().stream()
                .map(ProgramResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProgramResponse getProgram(String id) {
        return programRepository.findByIdAndDeletedAtIsNull(id)
                .map(ProgramResponse::from)
                .orElseThrow(() -> new ProgramNotFoundException(id));
    }

    @Transactional
    public void updateProgram(String id, ProgramUpdateRequest request) {
        Program program = programRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));

        program.update(request);
    }

    @Transactional
    public void deleteProgram(String id) {
        Program program = programRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));

        program.delete();
    }
}
