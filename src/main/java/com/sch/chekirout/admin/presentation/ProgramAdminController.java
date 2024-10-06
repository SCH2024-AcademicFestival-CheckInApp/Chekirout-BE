package com.sch.chekirout.admin.presentation;

import com.sch.chekirout.program.application.ProgramService;
import com.sch.chekirout.admin.application.dto.request.ProgramRegisterRequest;
import com.sch.chekirout.admin.application.dto.request.ProgramUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/programs")
@Tag(name = "Program Admin API", description = "프로그램 관리 API")
public class ProgramAdminController {

    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<Void> saveProgram(@Valid @RequestBody ProgramRegisterRequest request) {
        final String savedId = programService.saveProgram(request);
        final URI location = URI.create("/api/v1/admin/program/" + savedId);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{programId}")
    public ResponseEntity<Void> updateProgram(@PathVariable String programId, @Valid @RequestBody ProgramUpdateRequest request) {
        programService.updateProgram(programId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgram(@PathVariable String programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.noContent().build();
    }
}
