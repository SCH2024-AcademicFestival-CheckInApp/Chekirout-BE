package com.sch.chekirout.prizeDraw.presentation;

import com.sch.chekirout.prizeDraw.application.PrizeDrawService;
import com.sch.chekirout.prizeDraw.application.dto.response.DrawResult;
import com.sch.chekirout.prizeDraw.application.dto.response.PrizeWinnerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경품 추첨 성공"),
            @ApiResponse(responseCode = "400", description = "경품 ID가 잘못된 경우"),
            @ApiResponse(responseCode = "400", description = "경품 추첨 대상자가 충분하지 않은 경우"),
            @ApiResponse(responseCode = "404", description = "경품이 없는 경우")
    })
    @PostMapping("/draw")
    public ResponseEntity<DrawResult> drawPrizeWinners(
            @RequestParam Long prizeId,
            @RequestParam Integer numberOfWinners) {

        return ResponseEntity.ok(prizeDrawService.drawPrizeWinners(prizeId, numberOfWinners));
    }

    // 경품 수령 확인
    @Operation(summary = "경품 수령 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경품 수령 확인 성공"),
            @ApiResponse(responseCode = "400", description = "이미 수령한 경우"),
            @ApiResponse(responseCode = "404", description = "경품 당첨자가 없는 경우")
    })
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPrizeClaim(
            @RequestParam String prizeWinnerId) {

        prizeDrawService.confirmPrizeClaim(prizeWinnerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 경품 당첨자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 경품 당첨자 조회 성공")
    })
    @GetMapping("/winners")
    public ResponseEntity<List<PrizeWinnerResponse>> getPrizeWinners() {
        return ResponseEntity.ok(prizeDrawService.getAllPrizeWinners());
    }

    @Operation(summary = "특정 경품 당첨자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 경품 당첨자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "경품이 없는 경우")
    })
    @GetMapping("/{prizeId}/winners")
    public ResponseEntity<List<PrizeWinnerResponse>> getPrizeWinners(
            @PathVariable Long prizeId) {

        return ResponseEntity.ok(prizeDrawService.getPrizeWinners(prizeId));
    }

    @Operation(summary = "학번으로 경품 당첨자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 학번으로 경품 당첨자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 학번으로 당첨자가 없는 경우")
    })
    @GetMapping("/winners/{studentId}")
    public ResponseEntity<List<PrizeWinnerResponse>> getPrizeWinnerByStudentId(
            @PathVariable String studentId) {

        return ResponseEntity.ok(prizeDrawService.getPrizeWinnersByStudentId(studentId));
    }
}
