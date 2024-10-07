package com.sch.chekirout.participation.application;

import com.sch.chekirout.admin.application.dto.response.CategoryResponse;
import com.sch.chekirout.admin.application.dto.response.ProgramResponse;
import com.sch.chekirout.participation.domain.ParticipationRecord;
import com.sch.chekirout.participation.domain.repository.ParticipationRecordRepository;
import com.sch.chekirout.program.application.CategoryService;
import com.sch.chekirout.program.application.ProgramService;
import com.sch.chekirout.program.application.dto.response.ParticipationRecordResponse;
import com.sch.chekirout.program.application.dto.response.ProgramParticipationHistories;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.exception.ProgramNotFoundException;
import com.sch.chekirout.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRecordRepository participationRecordRepository;
    private final ProgramService programService;
    private final CategoryService categoryService;

    public ProgramParticipationHistories getParticipationHistories(User user) {
        List<ParticipationRecordResponse> participationRecordResponses = participationRecordRepository.findAllByUserId(user.getId())
                .stream()
                .map(participationRecord -> {
                    ProgramResponse program = programService.getProgram(participationRecord.getProgramId());
                    CategoryResponse category = categoryService.getCategory(program.getCategoryId());
                    return ParticipationRecordResponse.from(program, category, participationRecord);
                })
                .toList();

        return ProgramParticipationHistories.from(user, participationRecordResponses);
    }
}
