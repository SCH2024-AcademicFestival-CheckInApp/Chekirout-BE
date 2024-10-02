package com.sch.chekirout.program.presentation;

import com.sch.chekirout.program.application.ProgramService;
import com.sch.chekirout.program.application.dto.request.ProgramRegisterRequest;
import com.sch.chekirout.program.application.dto.request.ProgramUpdateRequest;
import com.sch.chekirout.program.application.dto.response.ProgramResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ProgramResponse>> getPrograms() {
        return ResponseEntity.ok(programService.getPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponse> getProgram(@PathVariable String id) {
        return ResponseEntity.ok(programService.getProgram(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProgram(@PathVariable String id, @Valid @RequestBody ProgramUpdateRequest request) {
        programService.updateProgram(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable String id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
