package com.sch.chekirout.stampCard.presentation;


import com.sch.chekirout.stampCard.application.StampCardService;
import com.sch.chekirout.stampCard.application.dto.response.StampCardDetail;
import com.sch.chekirout.stampCard.application.dto.response.StampCardResponse;
import com.sch.chekirout.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/stamp-cards")
@Tag(name = "StampCard Admin API", description = "스탬프카드 관리자 API")
public class StampCardAdminController {

    private final StampCardService stampCardService;
    private final UserService userService;

    @Operation(summary = "모든 프로그램 참여 완료자 수 조회", description = "모든 프로그램 참여 완료자 수를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 프로그램 참여 완료자 수 조회 성공")
    })
    @GetMapping("/completed-counts")
    public ResponseEntity<Long> getCompletedPrizeCount() {
        Long completedCount = stampCardService.getCompletedPrizeCount();
        return ResponseEntity.ok(completedCount);
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<StampCardResponse>> getCompletedStampCardList(Pageable pageable) {
        return ResponseEntity.ok(stampCardService.getCompletedStampCards(pageable));
    }

    @Operation(summary = "스탬프카드로 학생 전체 참여 현황 조회", description = "스탬프카드로 학생 전체 참여 현황을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 전체 참여 현황 조회 성공")
    })
    @GetMapping
    public ResponseEntity<Page<StampCardResponse>> getStampCardList(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stampCardService.getStampCardList(pageable));
    }

    @Operation(summary = "스탬프카드로 특정 학생 참여 현황 조회", description = "스탬프카드로 특정 학생 참여 현황을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 학생 참여 현황 조회 성공"),
            @ApiResponse(responseCode = "404", description = "특정 학생 참여 현황 없음")
    })
    @GetMapping("/{username}")
    public ResponseEntity<StampCardDetail> getStampCardDetail(@PathVariable String username) {
        return ResponseEntity.ok(stampCardService.getStampCardDetailByStduentId(username));
    }
}
