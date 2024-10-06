package com.sch.chekirout.program.presentation;

import com.sch.chekirout.program.application.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/{programId}/participate")
    public ResponseEntity<Void> participateInProgram(
            @PathVariable String programId,
            @RequestBody ProgramParticipationRequest request) {
        programService.participateInProgram(programId, request);
        return ResponseEntity.ok().build();
    }
}
