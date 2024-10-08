package com.sch.chekirout.stampCard.presentation;

import com.sch.chekirout.stampCard.application.StampCardService;
import com.sch.chekirout.stampCard.application.dto.response.StampCardDetail;
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
@RequestMapping("/api/v1/stamp-cards")
@Tag(name = "StampCard API", description = "스탬프카드 API")
public class StampCardController {

    private final StampCardService stampCardService;
    private final UserService userService;

    @Operation(summary = "스탬프카드 조회", description = "스탬프카드를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스탬프카드 조회 성공"),
            @ApiResponse(responseCode = "404", description = "스탬프카드 없음")
    })
    @GetMapping
    public ResponseEntity<StampCardDetail> getStampCard() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findUserByUsername(currentUsername);

        return ResponseEntity.ok(stampCardService.getStampCardDetail(user));
    }

    @GetMapping("/eligible")
    public ResponseEntity<Long> getEligableForPrizeCount() {
        Long eligibleCount = stampCardService.getEligibleForPrizeCount();
        return ResponseEntity.ok(eligibleCount);
    }
}
