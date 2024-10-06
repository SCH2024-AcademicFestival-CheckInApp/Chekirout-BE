package com.sch.chekirout.program.presentation;

import com.sch.chekirout.admin.application.dto.response.ProgramResponse;
import com.sch.chekirout.program.application.ProgramParticipationService;
import com.sch.chekirout.program.application.ProgramService;
import com.sch.chekirout.program.application.dto.request.ProgramParticipationRequest;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/programs")
@Tag(name = "Program API", description = "프로그램 참여 API")
public class ProgramController {

    private final ProgramService programService;
    private final ProgramParticipationService programParticipationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ProgramResponse>> getPrograms() {
        return ResponseEntity.ok(programService.getPrograms());
    }

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramResponse> getProgram(@PathVariable String programId) {
        return ResponseEntity.ok(programService.getProgram(programId));
    }

    @PostMapping("/{programId}/participate")
    public ResponseEntity<Void> participateInProgram(
            @PathVariable String programId,
            @RequestBody ProgramParticipationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findUserByUsername(currentUsername);

        programParticipationService.participateInProgram(user, programId, request);
        return ResponseEntity.ok().build();
    }
}
