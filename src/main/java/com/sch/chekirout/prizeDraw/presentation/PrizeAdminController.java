package com.sch.chekirout.prizeDraw.presentation;

import com.sch.chekirout.prizeDraw.application.PrizeService;
import com.sch.chekirout.prizeDraw.application.dto.request.PrizeRequest;
import com.sch.chekirout.prizeDraw.application.dto.response.PrizeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/prizes")
@Tag(name = "Prize Admin API", description = "경품 관리자 API")
public class PrizeAdminController {

    private final PrizeService prizeService;

    @Operation(summary = "경품 생성", description = "경품을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "경품 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 경품 이름")
    })
    @PostMapping
    public ResponseEntity<Void> savePrize(@Valid @RequestBody PrizeRequest request) {
        final Long savedId = prizeService.savePrize(request);
        final URI location = URI.create("/api/v1/admin/prizes/" + savedId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "경품 목록 조회", description = "모든 경품 목록을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경품 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
    })
    @GetMapping
    public ResponseEntity<List<PrizeResponse>> getPrizes() {
        final List<PrizeResponse> prizes = prizeService.getPrizes();
        return ResponseEntity.ok(prizes);
    }

    @Operation(summary = "경품 조회(prizeId)", description = "경품을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경품 조회 성공"),
            @ApiResponse(responseCode = "404", description = "경품 없음")
    })
    @GetMapping("/{prizeId}")
    public ResponseEntity<PrizeResponse> getPrize(@PathVariable Long prizeId) {
        final PrizeResponse prize = prizeService.getPrize(prizeId);
        return ResponseEntity.ok(prize);
    }

    @Operation(summary = "경품 수정(prizeId)", description = "경품을 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경품 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경품 없음")
    })
    @PutMapping("/{prizeId}")
    public ResponseEntity<Void> updatePrize(@PathVariable Long prizeId,
                                            @Valid @RequestBody PrizeRequest request) {
        prizeService.updatePrize(prizeId, request);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "경품 삭제(prizeId)", description = "경품을 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경품 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경품 없음")
    })
    @DeleteMapping("/{prizeId}")
    public ResponseEntity<Void> deletePrize(@PathVariable Long prizeId) {
        prizeService.deletePrize(prizeId);
        return ResponseEntity.ok().build();
    }
}
