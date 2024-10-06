package com.sch.chekirout.program.presentation;

import com.sch.chekirout.admin.application.dto.response.ProgramResponse;
import com.sch.chekirout.program.application.ProgramParticipationService;
import com.sch.chekirout.program.application.ProgramService;
import com.sch.chekirout.program.application.dto.request.ProgramParticipationRequest;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "프로그램 조회", description = "프로그램을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로그램 조회 성공"),
            @ApiResponse(responseCode = "404", description = "프로그램 없음")
    })
    @GetMapping("/{programId}")
    public ResponseEntity<ProgramResponse> getProgram(@PathVariable String programId) {
        return ResponseEntity.ok(programService.getProgram(programId));
    }

    @Operation(summary = "프로그램 참여", description = "프로그램에 참여하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로그램 참여 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(이미 참여한 프로그램, 참여 시간 및 위치 오류)"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "프로그램 없음")
    })
    @PostMapping("/{programId}/participate")
    public ResponseEntity<String> participateInProgram(
            @PathVariable String programId,
            @RequestBody ProgramParticipationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findUserByUsername(currentUsername);

        programParticipationService.participateInProgram(user, programId, request);
        return ResponseEntity.ok().body("프로그램 참여가 완료되었습니다.");
    }
}
