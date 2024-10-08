package com.sch.chekirout.stampCard.presentation;


import com.sch.chekirout.stampCard.application.StampCardService;
import com.sch.chekirout.user.application.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/stamp-cards")
@Tag(name = "StampCard Admin API", description = "스탬프카드 관리자 API")
public class StampCardAdminController {

    private final StampCardService stampCardService;
    private final UserService userService;

    @GetMapping("/completed")
    public ResponseEntity<Long> getCompletedPrizeCount() {
        Long completedCount = stampCardService.getCompletedPrizeCount();
        return ResponseEntity.ok(completedCount);
    }
}
