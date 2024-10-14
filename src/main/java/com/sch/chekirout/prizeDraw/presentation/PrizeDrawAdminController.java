package com.sch.chekirout.prizeDraw.presentation;

import com.sch.chekirout.prizeDraw.application.PrizeDrawService;
import com.sch.chekirout.prizeDraw.application.dto.response.DrawResult;
import com.sch.chekirout.prizeDraw.application.dto.response.PrizeWinnerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/prize-draws")
@Tag(name = "Prize Draw Admin API", description = "경품 추첨 관리 API")
public class PrizeDrawAdminController {

    private final PrizeDrawService prizeDrawService;

    @Operation(summary = "경품 추첨")
    @ApiResponses
    @PostMapping("/draw")
    public ResponseEntity<DrawResult> drawPrizeWinners(
            @RequestParam Long prizeId,
            @RequestParam Integer numberOfWinners) {

        return ResponseEntity.ok(prizeDrawService.drawPrizeWinners(prizeId, numberOfWinners));
    }

    // 경품 수령 확인
    @Operation(summary = "경품 수령 확인")
    @ApiResponses
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPrizeClaim(
            @RequestParam String prizeWinnerId) {

        prizeDrawService.confirmPrizeClaim(prizeWinnerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/winners")
    public ResponseEntity<List<PrizeWinnerResponse>> getPrizeWinners() {
        return ResponseEntity.ok(prizeDrawService.getAllPrizeWinners());
    }

    // 경품 별 당첨자 목록 조회
    @GetMapping("/{prizeId}/winners")
    public ResponseEntity<List<PrizeWinnerResponse>> getPrizeWinners(
            @PathVariable Long prizeId) {

        return ResponseEntity.ok(prizeDrawService.getPrizeWinners(prizeId));
    }
}
