package com.sch.chekirout.participation.presentation;

import com.sch.chekirout.participation.application.ParticipationService;
import com.sch.chekirout.program.application.dto.response.ProgramParticipationHistories;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participation-records")
@Tag(name = "Participation Record API", description = "참여 기록 API")
public class ParticipationRecordController {

    private final ParticipationService participationService;
    private final UserService userService;

    @Operation(summary = "내 참여 기록 조회", description = "내가 참여한 프로그램의 기록을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여 기록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ProgramParticipationHistories> getMyParticipations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findUserByUsername(currentUsername);

        return ResponseEntity.ok(participationService.getParticipationHistories(user));
    }
}
